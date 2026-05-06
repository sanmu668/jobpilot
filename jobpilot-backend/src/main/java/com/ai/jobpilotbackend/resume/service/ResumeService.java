package com.ai.jobpilotbackend.resume.service;

import com.ai.jobpilotbackend.resume.dto.AnalyzeRequest;
import com.ai.jobpilotbackend.resume.entity.Resume;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wzs
 * @date 2026年05月03日 23:01
 */
public interface ResumeService {

    Long upload(MultipartFile file, Long userId) throws Exception;

    void parseAsync(Long resumeId);

    Resume getResumeById(Long resumeId);

    /**
     * 提交 AI 匹配分析任务，立即返回 taskId，前端轮询 /ai/result/{taskId} 获取结果
     */
    Long analyzeResume(AnalyzeRequest request, Long userId);
}
