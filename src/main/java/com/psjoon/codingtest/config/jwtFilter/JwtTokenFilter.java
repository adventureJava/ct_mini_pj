package com.psjoon.codingtest.config.jwtFilter;

import com.psjoon.codingtest.entity.Member;
import com.psjoon.codingtest.service.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtTokenProvider.getUsername(token);
            // Member 엔티티로 유저 정보를 로드
            Member member = (Member) customUserDetailsService.loadUserByUsername(username);

            if (member != null) {
                // 권한 정보를 Member에서 가져오기
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        member, null, member.getAuthorities());  // 권한은 Collection<? extends GrantedAuthority>
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // 디버깅용 로그 추가
                System.out.println("인증 성공: " + username);
            }
        }else{System.out.println("인증 실패 또는 토큰 없음");}
        filterChain.doFilter(request, response);
    }


    private String resolveToken(HttpServletRequest request) {
        // 헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization");

        // 쿠키에서 토큰 추출 (헤더에 토큰이 없을 경우)
        if (bearerToken == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("Authorization")) {
                        bearerToken = cookie.getValue();
                    }
                }
            }
        }

        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(6); // "Bearer " 이후의 토큰 반환
        }
        return null;
    }
}
