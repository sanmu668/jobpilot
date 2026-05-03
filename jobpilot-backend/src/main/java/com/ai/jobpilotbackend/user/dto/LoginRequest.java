package com.ai.jobpilotbackend.user.dto;

import lombok.Data;

/**
 * @author wzs
 * @date 2026年05月03日 16:40
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
