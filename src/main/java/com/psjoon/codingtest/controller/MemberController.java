package com.psjoon.codingtest.controller;


import com.psjoon.codingtest.config.jwtFilter.JwtTokenFilter;
import com.psjoon.codingtest.config.jwtFilter.JwtTokenProvider;
import com.psjoon.codingtest.dto.MemberRequest;
import com.psjoon.codingtest.entity.Authority;
import com.psjoon.codingtest.entity.Member;
import com.psjoon.codingtest.service.AuthorityService;
import com.psjoon.codingtest.service.CustomUserDetailsService;
import com.psjoon.codingtest.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/join")
    public ResponseEntity<Member> save(@RequestBody MemberRequest request) {
        Member member = request.getMember();
        Authority authority = request.getAuthority();

        Member savedMember = memberService.save(member);
        authority.setMember(savedMember); // Member와 Authority의 관계 설정
        authorityService.save(authority);

        return new ResponseEntity<>(savedMember, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberRequest request, HttpServletResponse response) {
        Member member = request.getMember();
        Member loginMember = memberService.login(member);

        System.out.println(loginMember);

        if (loginMember != null) {
            String token = jwtTokenProvider.createToken(loginMember.getId(), getRoleFromAuthorities(loginMember));
            // JWT 토큰을 HTTP-Only 쿠키에 저장
            Cookie cookie = new Cookie("Authorization", "Bearer" + token);
            cookie.setHttpOnly(true); // JavaScript에서 접근할 수 없도록 설정
            cookie.setMaxAge(60 * 60); // 쿠키 유효 기간 (1시간)
            cookie.setPath("/"); // 모든 경로에서 쿠키가 전송되도록 설정
            response.addCookie(cookie);
            return ResponseEntity.ok("로그인 성공");
        } else {
            System.out.println("로그인실패");
            // 로그인 실패
            return new ResponseEntity<>("로그인 실패", HttpStatus.UNAUTHORIZED);
        }
    }

    // 권한 추출 메서드 (Authority 엔티티에서 authorityName을 추출)
    private String getRoleFromAuthorities(Member member) {
        return member.getAuthority().stream()
                .map(Authority::getAuthorityName) // 각 Authority 엔티티에서 권한 이름을 추출
                .findFirst()  // 여러 권한이 있을 경우 첫 번째 권한을 선택
                .orElse("ROLE_USER");  // 기본 값으로 ROLE_USER 반환
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkAuthStatus(HttpServletRequest request) {
        // 요청에서 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();

        // 토큰이 존재하고 유효한지 검증
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);

            // 인증된 사용자 정보를 가져오기
            Member member = (Member) customUserDetailsService.loadUserByUsername(username);

            if (member != null) {
                System.out.println(getRoleFromAuthorities(member));
                response.put("authenticated", true);
                response.put("username", member.getUsername());  // 유저 이름 정보 반환
                response.put("role", getRoleFromAuthorities(member));   // 유저 권한 정보 반환
                return ResponseEntity.ok(response);  // 200 OK와 함께 응답
            }
        }

        // 인증되지 않은 경우 응답
        response.put("authenticated", false);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
