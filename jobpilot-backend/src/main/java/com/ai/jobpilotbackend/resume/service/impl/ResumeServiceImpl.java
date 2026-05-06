package com.ai.jobpilotbackend.resume.service.impl;

import com.ai.jobpilotbackend.resume.dto.AnalyzeRequest;
import com.ai.jobpilotbackend.resume.entity.Resume;
import com.ai.jobpilotbackend.resume.entity.ResumeDetail;
import com.ai.jobpilotbackend.resume.mapper.ResumeDetailMapper;
import com.ai.jobpilotbackend.resume.mapper.ResumeMapper;
import com.ai.jobpilotbackend.resume.service.ResumeService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

/**
 * @author wzs
 * @date 2026年05月03日 23:02
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    @Value("${file.upload-path}")
    private String uploadDir;

    private final ResumeMapper resumeMapper;
    private final ResumeDetailMapper detailMapper;
    private final AiTaskService aiTaskService;
    private final RestTemplate restTemplate = new RestTemplate();

    public ResumeServiceImpl(ResumeMapper resumeMapper,
                             ResumeDetailMapper detailMapper,
                             AiTaskService aiTaskService) {
        this.resumeMapper = resumeMapper;
        this.detailMapper = detailMapper;
        this.aiTaskService = aiTaskService;
    }

    @Override
    public Long upload(MultipartFile file, Long userId) throws Exception {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + suffix;

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = uploadDir + newFileName;
        file.transferTo(new File(filePath));

        String rawText = parseFile(filePath, suffix);

        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setFileName(originalFilename);
        resume.setFileUrl(filePath);
        resume.setFileType(suffix);
        resume.setRawText(rawText);
        resume.setParseStatus(0);
        resumeMapper.insert(resume);

        return resume.getId();
    }

    @Override
    public Resume getResumeById(Long resumeId) {
        return resumeMapper.findById(resumeId);
    }

    @Override
    @Async("taskExecutor")
    public void parseAsync(Long resumeId) {
        try {
            Resume resume = resumeMapper.findById(resumeId);
            if (resume == null) {
                throw new RuntimeException("简历不存在");
            }

            String result = callPython(resume.getRawText());

            ObjectMapper mapper = new ObjectMapper();
            if (result.startsWith("\"")) {
                result = mapper.readValue(result, String.class);
            }
            JsonNode json = mapper.readTree(result);

            ResumeDetail detail = new ResumeDetail();
            //如果简历详情表已经存在，则更新，否则插入
            if (detail.getResumeId() != null){
                detailMapper.update(detail,resumeId);
                return;
            }
            detail.setResumeId(resumeId);
            detail.setName(getText(json, "name"));
            detail.setPhone(getText(json, "phone"));
            detail.setEmail(getText(json, "email"));
            detail.setSkills(json.has("skills") ? json.get("skills").toString() : "[]");
            detail.setEducation(json.has("education") ? json.get("education").toString() : "[]");
            detail.setExperience(json.has("experience") ? json.get("experience").toString() : "[]");
            detail.setSkillTags(extractSkills(json));

            detailMapper.insert(detail);
            resumeMapper.updateParseStatus(resumeId, 1);

        } catch (Exception e) {
            e.printStackTrace();
            resumeMapper.updateParseStatus(resumeId, 2);
        }
    }

    @Override
    public Long analyzeResume(AnalyzeRequest request, Long userId) {
        Resume resume = resumeMapper.findById(request.getResumeId());
        if (resume == null || resume.getParseStatus() != 1) {
            throw new RuntimeException("简历不存在或未解析完成");
        }
        return aiTaskService.submitTask(userId, request.getResumeId(), request.getJobDescription());
    }

    private String parseFile(String filePath, String suffix) throws Exception {
        if (".pdf".equalsIgnoreCase(suffix)) {
            try (PDDocument document = PDDocument.load(new File(filePath))) {
                return new PDFTextStripper().getText(document);
            }
        }
        // TODO: 支持 docx 格式
        return "";
    }

    private String callPython(String rawText) {
        String url = "http://localhost:8000/api/resume/ai/parse";
        Map<String, String> body = Map.of(
                "text", rawText.length() > 6000 ? rawText.substring(0, 6000) : rawText
        );
        return restTemplate.postForObject(url, body, String.class);
    }

    private String getText(JsonNode json, String field) {
        if (json == null || !json.has(field) || json.get(field).isNull()) {
            return "";
        }
        return json.get(field).asText();
    }

    private String extractSkills(JsonNode json) {
        if (!json.has("skills")) return "";
        List<String> skills = new ArrayList<>();
        json.get("skills").forEach(node -> skills.add(node.asText()));
        return String.join(",", skills);
    }
}
