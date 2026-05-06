package com.ai.jobpilotbackend.resume.controller;

import com.ai.jobpilotbackend.resume.dto.AnalyzeRequest;
import com.ai.jobpilotbackend.resume.entity.AiTask;
import com.ai.jobpilotbackend.resume.entity.Resume;
import com.ai.jobpilotbackend.resume.mapper.AitaskMapper;
import com.ai.jobpilotbackend.resume.service.ResumeService;
import com.ai.jobpilotbackend.resume.vo.AnalyzeResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author wzs
 * @date 2026年05月03日 23:20
 */
@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final AitaskMapper aitaskMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeController(ResumeService resumeService, AitaskMapper aitaskMapper) {
        this.resumeService = resumeService;
        this.aitaskMapper = aitaskMapper;
    }

    @PostMapping("/upload")
    public Map<String, Object> upload(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws Exception {

        Long userId = Long.valueOf(request.getAttribute("userId").toString());
        Long resumeId = resumeService.upload(file, userId);

        return Map.of("code", 200, "message", "上传成功", "resumeId", resumeId);
    }

    /**
     * 提交简历结构化解析任务（异步），立即返回
     */
    @PostMapping("/parse/{id}")
    public Map<String, Object> parse(@PathVariable("id") Long resumeId) {
        resumeService.parseAsync(resumeId);
        return Map.of("code", 200, "message", "解析任务已提交");
    }

    /**
     * 提交 AI 匹配分析任务（异步），返回 taskId，前端用 taskId 轮询结果
     */
    @PostMapping("/analyze")
    public Map<String, Object> analyze(
            @RequestBody AnalyzeRequest request,
            HttpServletRequest httpRequest) {

        Long userId = Long.valueOf(httpRequest.getAttribute("userId").toString());
        Long taskId = resumeService.analyzeResume(request, userId);

        return Map.of("code", 200, "message", "分析任务已提交", "taskId", taskId);
    }

    /**
     * 查询简历解析状态（前端用于判断 parse 完成后再提交 analyze）
     * parseStatus: 0=解析中  1=解析完成  2=解析失败
     */
    @GetMapping("/parse/status/{resumeId}")
    public Map<String, Object> parseStatus(@PathVariable Long resumeId) {
        Resume resume = resumeService.getResumeById(resumeId);
        if (resume == null) {
            return Map.of("code", 404, "message", "简历不存在");
        }
        String status = switch (resume.getParseStatus()) {
            case 1  -> "done";
            case 2  -> "failed";
            default -> "processing";
        };
        return Map.of("code", 200, "status", status);
    }

    /**
     * 轮询 AI 分析结果
     * status: processing=分析中  done=完成  failed=失败
     */
    @GetMapping("/ai/result/{taskId}")
    public Map<String, Object> getResult(@PathVariable Long taskId) {
        AiTask task = aitaskMapper.findById(taskId);

        if (task == null) {
            return Map.of("code", 404, "message", "任务不存在");
        }
        if (task.getStatus() == 0) {
            return Map.of("code", 200, "status", "processing", "message", "分析中，请稍后重试");
        }
        if (task.getStatus() == 2) {
            return Map.of("code", 500, "status", "failed", "message", "AI分析失败");
        }

        try {
            AnalyzeResponse result = objectMapper.readValue(task.getResult(), AnalyzeResponse.class);
            return Map.of("code", 200, "status", "done", "result", result);
        } catch (Exception e) {
            return Map.of("code", 500, "status", "failed", "message", "结果解析失败");
        }
    }
}
