package com.ai.jobpilotbackend.user.service.impl;

import com.ai.jobpilotbackend.user.dto.LoginRequest;
import com.ai.jobpilotbackend.user.dto.RegisterRequest;
import com.ai.jobpilotbackend.user.entity.User;
import com.ai.jobpilotbackend.user.jwt.JwtUtil;
import com.ai.jobpilotbackend.user.mapper.UserMapper;
import com.ai.jobpilotbackend.user.service.UserService;
import com.ai.jobpilotbackend.user.vo.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author wzs
 * @date 2026年05月03日 16:57
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByEmail(request.getEmail());

        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户被禁用");
        }
        String token = JwtUtil.generateToken(user.getId(), user.getRole());

        return new LoginResponse(
                token,
                user.getRole(),
                user.getUsername()
        );
    }

    @Override
    public void register(RegisterRequest request) {
        //1. 判断用户是否存在
        User existUser = userMapper.findByEmail(request.getEmail());
        if (existUser != null) {
            throw new RuntimeException("用户已存在");
        }
        //2.密码加密
        String password = passwordEncoder.encode(request.getPassword());

        //3.构建用户
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(password);
        user.setUsername(request.getUsername());
        user.setRole("user");
        user.setStatus(1);
        userMapper.insert(user);
    }
}
