package com.ai.jobpilotbackend.config;

import com.ai.jobpilotbackend.user.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author wzs
 * @date 2026年05月03日
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = JwtUtil.parseTokens(token);
                String userId = claims.getSubject();
                request.setAttribute("userId", userId);
            } catch (Exception e) {
                // Invalid token — leave userId attribute unset; downstream security handles it
            }
        }

        filterChain.doFilter(request, response);
    }
}
