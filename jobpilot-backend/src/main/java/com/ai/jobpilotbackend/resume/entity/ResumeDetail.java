package com.ai.jobpilotbackend.resume.entity;

import lombok.Data;
import org.apache.ibatis.annotations.Insert;

import java.time.LocalDateTime;

/**
 * @author wzs
 * @date 2026年05月04日 15:47
 */
@Data
public class ResumeDetail {
    private Long resumeId;
    private String name;
    private String phone;
    private String email;

    private String skills;
    private String education;
    private String experience;

    private String skillTags;

    private Integer parseStatus = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
