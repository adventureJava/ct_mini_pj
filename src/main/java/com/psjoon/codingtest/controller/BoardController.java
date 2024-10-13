package com.psjoon.codingtest.controller;

import com.psjoon.codingtest.dto.CodeRequest;
import com.psjoon.codingtest.entity.Member;
import com.psjoon.codingtest.entity.TestBoard;
import com.psjoon.codingtest.entity.TestRecord;
import com.psjoon.codingtest.repository.MemberRepository;
import com.psjoon.codingtest.service.MemberService;
import com.psjoon.codingtest.service.TestBoardService;
import com.psjoon.codingtest.service.TestRecordService;
import com.psjoon.codingtest.util.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

@Controller
public class BoardController {
    @Autowired
    TestBoardService testBoardService;

    @Autowired
    TestRecordService testRecordService;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/test_board")
    public String goTestBoard(@RequestParam(defaultValue = "1") int pageNum,@RequestParam(required = false) String source, Model model) {
        int listCnt = 5; // 한 페이지에 표시할 게시글 수;

        String urlPrefix = "";
        if ("test".equals(source)) {
            urlPrefix = "test_board?source=test";
        } else {
            urlPrefix = "test_board?source=exam";
        }

        // 페이징 처리된 게시글 목록 가져오기
        List<TestBoard> testBoardList = testBoardService.getTestBoards(pageNum, listCnt);
        model.addAttribute("list", testBoardList);

        // 페이징 처리 HTML 생성
        PagingUtil paging = testBoardService.getPaging(pageNum, listCnt);
        String pageHtml = paging.makePaging(urlPrefix);
        model.addAttribute("paging", pageHtml);

        if ("test".equals(source)) {
            return "test/test_board";
        } else {
            return "test/exam_board";
        }
    }

    @GetMapping("/test")
    public String gotest(@RequestParam("tId") Integer tId, Model model) {
        TestBoard dto = testBoardService.findById(tId);
        model.addAttribute("board", dto);
        return "test/test";
    }

    @PostMapping("/testSave")
    public String TestSave(@RequestParam String tId, @RequestParam String code) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        // 1. Member 객체 가져오기 (userId로 검색)
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 없습니다."));

        // 2. TestBoard 객체 가져오기 (tId로 검색)
        TestBoard testBoard = testBoardService.findById(Integer.parseInt(tId));

        // 3. TestRecord 객체 생성 및 값 설정
        TestRecord testRecord = new TestRecord();
        testRecord.setMember(member); // Member 설정
        testRecord.setTestBoard(testBoard); // TestBoard 설정
        testRecord.setCode(code); // Code 설정
        testRecord.setTWriteTime(new Date()); // 현재 시간 설정

        // 4. TestRecord 저장
        testRecordService.save(testRecord);

        return "redirect:/testRecord";

    }

    @GetMapping("/testRecord")
    public String goTestRecord(@RequestParam(defaultValue = "1") int pageNum, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        String urlPrefix = "testRecord";

        int listCnt = 5; // 한 페이지에 표시할 게시글 수;

        // 페이징 처리된 게시글 목록 가져오기
        List<TestRecord> testBoardList = testRecordService.getTestRecords(pageNum, listCnt,userId);
        model.addAttribute("list", testBoardList);

        // 페이징 처리 HTML 생성
        PagingUtil paging = testRecordService.getPaging(pageNum, listCnt,userId);
        String pageHtml = paging.makePaging(urlPrefix);
        model.addAttribute("paging", pageHtml);

        return "test/test_Record"; // 결과를 보여줄 뷰
    }

    @GetMapping("/exam")
    public String goexam(@RequestParam("tId") Integer tId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        model.addAttribute("userId", userId);
        TestBoard dto = testBoardService.findById(tId);
        model.addAttribute("board", dto);
        return "test/exam";
    }

}
