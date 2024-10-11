package com.psjoon.codingtest.config.jwtFilter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long validityInMilliseconds = 3600000; // 1시간

    // JWT 토큰 생성
    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey) // 환경 변수에서 불러온 비밀 키로 서명
                .compact();
    }

    // JWT 토큰에서 정보 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었을 때 처리
            System.out.println("토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰 서명 오류 또는 잘못된 토큰 처리
            System.out.println("토큰 검증 실패: " + e.getMessage());
        }
        return false;
    }
}

