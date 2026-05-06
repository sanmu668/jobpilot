package com.ai.jobpilotbackend.resume.service.impl;

import com.ai.jobpilotbackend.resume.entity.AiTask;
import com.ai.jobpilotbackend.resume.entity.Resume;
import com.ai.jobpilotbackend.resume.entity.ResumeDetail;
import com.ai.jobpilotbackend.resume.mapper.AitaskMapper;
import com.ai.jobpilotbackend.resume.mapper.ResumeDetailMapper;
import com.ai.jobpilotbackend.resume.mapper.ResumeMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wzs
 * @date 2026年05月06日 0:39
 */
@Service
public class AiAsyncService {

    private final AitaskMapper taskMapper;
    private final ResumeMapper resumeMapper;
    private final ResumeDetailMapper detailMapper;
    private final StringRedisTemplate redisTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    public AiAsyncService(AitaskMapper taskMapper,
                          ResumeMapper resumeMapper,
                          ResumeDetailMapper detailMapper,
                          StringRedisTemplate redisTemplate) {
        this.taskMapper = taskMapper;
        this.resumeMapper = resumeMapper;
        this.detailMapper = detailMapper;
        this.redisTemplate = redisTemplate;
    }

    @Async("taskExecutor")
    public void process(Long taskId) {
        try {
            AiTask task = taskMapper.findById(taskId);
            ResumeDetail detail = detailMapper.findByResumeId(task.getResumeId());

            String job = task.getJobDesc() == null ? "" : task.getJobDesc();

            String structuredSnapshot = buildStructuredSnapshot(detail);
            String key = "ai:match:" + md5(structuredSnapshot + job);

            String cache = redisTemplate.opsForValue().get(key);
            if (cache != null) {
                taskMapper.updateResult(taskId, cache, 1);
                return;
            }

            String result = callPython(job, detail);

            redisTemplate.opsForValue().set(key, result, 1, TimeUnit.DAYS);
            taskMapper.updateResult(taskId, result, 1);

        } catch (Exception e) {
            e.printStackTrace();
            taskMapper.updateStatus(taskId, 2);
        }
    }

    private String callPython(String jobDesc, ResumeDetail detail) {
        String url = "http://localhost:8000/api/resume/ai/analyze";

        List<String> skillTags = new ArrayList<>();
        if (detail != null && detail.getSkillTags() != null && !detail.getSkillTags().isEmpty()) {
            skillTags = Arrays.asList(detail.getSkillTags().split(","));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("skills", detail != null ? detail.getSkills() : "");
        body.put("education", detail != null ? detail.getEducation() : "");
        body.put("experience", detail != null ? detail.getExperience() : "");
        body.put("skill_tags", skillTags);
        body.put("job_description", jobDesc);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, request, String.class);
    }

    private String buildStructuredSnapshot(ResumeDetail detail) {
        if (detail == null) return "";
        return String.join("|",
                detail.getSkills() == null ? "" : detail.getSkills(),
                detail.getEducation() == null ? "" : detail.getEducation(),
                detail.getExperience() == null ? "" : detail.getExperience(),
                detail.getSkillTags() == null ? "" : detail.getSkillTags()
        );
    }

    private String md5(String input) {
        return DigestUtils.md5DigestAsHex(input.getBytes());
    }
}
