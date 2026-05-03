package com.ai.jobpilotbackend.user.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * @author wzs
 * @date 2026年05月03日 16:48
 */
public class JwtUtil {
    // HS256 requires a key of at least 32 bytes
    private static final String SECRET_KEY = "jobpilot-secret-key-must-be-32bytes!";
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 86400000;  //1天

    public static String generateToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY)
                .compact();
    }

    public static Claims parseTokens(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
