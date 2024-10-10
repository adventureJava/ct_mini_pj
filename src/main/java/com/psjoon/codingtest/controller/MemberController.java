package com.psjoon.codingtest.controller;

import com.psjoon.codingtest.dto.MemberRequest;
import com.psjoon.codingtest.entity.Authority;
import com.psjoon.codingtest.entity.Member;
import com.psjoon.codingtest.service.AuthorityService;
import com.psjoon.codingtest.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    AuthorityService authorityService;


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
            // 로그인 성공
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            System.out.println(userId);
            return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
        } else {
            // 로그인 실패
            return new ResponseEntity<>("로그인 실패", HttpStatus.UNAUTHORIZED);
        }
    }
}
