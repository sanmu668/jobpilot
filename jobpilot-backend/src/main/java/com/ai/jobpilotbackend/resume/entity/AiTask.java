package com.ai.jobpilotbackend.resume.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wzs
 * @date 2026年05月06日 0:40
 */
@Data
public class AiTask {
    private Long id;
    private Long userId;
    private Long resumeId;
    private String jobDesc;
    private Integer status;
    private String result;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
