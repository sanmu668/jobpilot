package com.ai.jobpilotbackend.resume.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wzs
 * @date 2026年05月03日 22:58
 */
@Data
public class Resume {
    private Long id;
    private Long userId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private String rawText;
    private Integer parseStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
