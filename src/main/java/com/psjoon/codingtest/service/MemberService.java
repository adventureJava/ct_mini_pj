package com.psjoon.codingtest.service;

import com.psjoon.codingtest.entity.Member;
import com.psjoon.codingtest.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 주입받은 PasswordEncoder

    public Member save(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return member;
    }

    public Member login(Member member) {
        // 데이터베이스에서 회원 정보 조회
        Optional<Member> foundMember = memberRepository.findById(member.getId());

        if (foundMember.isPresent()) {
            // 비밀번호 일치 여부 확인
            if (passwordEncoder.matches(member.getPassword(), foundMember.get().getPassword())) {
                return foundMember.get();
            }
        }
        return null; // 로그인 실패 시 null 반환
    }
}
