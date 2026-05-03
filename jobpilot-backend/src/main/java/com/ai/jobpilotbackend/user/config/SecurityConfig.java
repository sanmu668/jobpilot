package com.ai.jobpilotbackend.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author wzs
 * @date 2026年05月03日 17:36
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // 关闭CSRF（开发阶段必须关）
                .authorizeHttpRequests(auth -> auth
                        // 放行接口
                        .requestMatchers("/api/user/login", "/api/user/register").permitAll()
                        // 其他接口需要认证
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // 关闭默认登录弹窗
                .formLogin(formLogin -> formLogin.disable());

        return http.build();
    }
}
