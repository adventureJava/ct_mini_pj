package com.psjoon.codingtest.controller;

import com.psjoon.codingtest.entity.Member;
import com.psjoon.codingtest.entity.TestBoard;
import com.psjoon.codingtest.service.TestBoardService;
import com.psjoon.codingtest.util.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

import java.util.List;

@Controller

public class BoardController {
    @Autowired
    TestBoardService testBoardService;

    @GetMapping("/test_board")
    public String goTestBoard(@RequestParam(defaultValue = "1") int pageNum, Model model) {
        int listCnt = 5; // 한 페이지에 표시할 게시글 수

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Member memberId = (Member) authentication.getPrincipal();
            System.out.println("ID는 "+memberId.getId());

        // 페이징 처리된 게시글 목록 가져오기
        List<TestBoard> testBoardList = testBoardService.getTestBoards(pageNum, listCnt);
        model.addAttribute("list", testBoardList);

        // 페이징 처리 HTML 생성
        PagingUtil paging = testBoardService.getPaging(pageNum, listCnt);
        String pageHtml = paging.makePaging();
        model.addAttribute("paging", pageHtml);

        return "test/test_board"; // 결과를 보여줄 뷰
    }
}
