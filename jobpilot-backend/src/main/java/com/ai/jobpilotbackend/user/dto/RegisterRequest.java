package com.ai.jobpilotbackend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author wzs
 * @date 2026年05月03日 17:09
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6")
    private String password;
    @NotBlank(message = "用户名不能为空")
    private String username;
}
