package com.ai.jobpilotbackend.resume.dto;

import lombok.Data;

/**
 * @author wzs
 * @date 2026年05月04日 23:59
 */
@Data
public class AnalyzeRequest {
    private Long resumeId;
    private String jobDescription;
}
