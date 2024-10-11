package com.psjoon.codingtest.controller;


import com.psjoon.codingtest.config.jwtFilter.JwtTokenProvider;
import com.psjoon.codingtest.dto.JwtResponse;
import com.psjoon.codingtest.dto.MemberRequest;
import com.psjoon.codingtest.entity.Authority;
import com.psjoon.codingtest.entity.Member;
import com.psjoon.codingtest.service.AuthorityService;
import com.psjoon.codingtest.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;



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

}
