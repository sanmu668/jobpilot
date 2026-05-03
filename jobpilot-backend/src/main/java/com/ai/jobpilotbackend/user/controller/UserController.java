package com.ai.jobpilotbackend.user.controller;

import com.ai.jobpilotbackend.user.dto.LoginRequest;
import com.ai.jobpilotbackend.user.dto.RegisterRequest;
import com.ai.jobpilotbackend.user.service.UserService;
import com.ai.jobpilotbackend.user.vo.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wzs
 * @date 2026年05月03日 17:02
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return "注册成功";
    }
}
