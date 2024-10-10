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
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> login(@RequestBody MemberRequest request) {
        Member member = request.getMember();

        Member loginMember = memberService.login(member);

        if (loginMember != null) {
            String token = jwtTokenProvider.createToken(loginMember.getId(), getRoleFromAuthorities(loginMember));

            // 토큰을 클라이언트에게 반환
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            // 로그인 실패
            return new ResponseEntity<>("로그인 실패", HttpStatus.UNAUTHORIZED);
        }
    }

    // 권한 추출 메서드
    private String getRoleFromAuthorities(Member member) {
        return member.getAuthorities().stream()
                .map(Authority::getAuthorityName) // 각 Authority에서 권한 이름 추출
                .findFirst()  // 여러 권한이 있을 경우 첫 번째 권한을 선택
                .orElse("ROLE_USER");  // 기본 값으로 ROLE_USER 반환
    }

    private final String key = "your-secret-key";

    public String createToken(String userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId); // 사용자 ID 설정
        claims.put("role", role); // 사용자 역할 설정

        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 토큰 유효 시간: 1시간

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, key) // 서명할 때 사용한 비밀 키
                .compact();
    }
}
