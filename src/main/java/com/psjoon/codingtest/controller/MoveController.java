package com.psjoon.codingtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MoveController {
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/join")
    public String signupForm() {
        return "member/join";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping("/mymenu")
    public String mymenu(Principal principal) {
        return "member/mymenu"; // 성공적으로 인증된 사용자에게 페이지 반환
    }

    @GetMapping("/test")
    public String gotest() {
        return "test/test";
    }

    @GetMapping("/exam")
    public String goexam() {
        return "test/exam";
    }

    @GetMapping("/supervise")
    public String examsupervise() {
        return "admin/supervise";
    }



}
