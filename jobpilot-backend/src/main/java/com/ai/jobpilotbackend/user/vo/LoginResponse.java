package com.ai.jobpilotbackend.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wzs
 * @date 2026年05月03日 16:41
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String role;
    private String username;
}
