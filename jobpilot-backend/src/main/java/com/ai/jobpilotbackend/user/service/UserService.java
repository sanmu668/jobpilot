package com.ai.jobpilotbackend.user.service;

import com.ai.jobpilotbackend.user.dto.LoginRequest;
import com.ai.jobpilotbackend.user.dto.RegisterRequest;
import com.ai.jobpilotbackend.user.vo.LoginResponse;

public interface UserService {
        LoginResponse login(LoginRequest request);

        void register(RegisterRequest request);
}
