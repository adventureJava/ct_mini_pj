package com.psjoon.codingtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {
    @GetMapping("/error/test-error")
    public String testError() {
        // 의도적으로 NullPointerException 발생
        String test = null;
        return test.toString(); // 이 줄에서 500 에러 발생
    }

    @GetMapping("/error/403")
    public String error403(HttpServletRequest request, Model model) {
        // 필요한 정보를 모델에 추가
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("status", "403");
        model.addAttribute("timestamp", System.currentTimeMillis()); // 현재 시간
        model.addAttribute("error", "접근 권한이 없습니다.");

        return "error/403"; // 403.html 반환
    }
}
