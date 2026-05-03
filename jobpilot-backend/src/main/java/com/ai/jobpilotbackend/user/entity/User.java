package com.ai.jobpilotbackend.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wzs
 * @date 2026年05月03日 16:35
 */
@Data
public class User {
    private Long id;
    private String email;
    private String password;
    private String username;
    private String avatarUrl;
    private String role;
    private Integer vipLevel;
    private LocalDateTime vipExpireTime;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
