package com.psjoon.codingtest.controller;

import com.psjoon.codingtest.entity.TestBoard;
import com.psjoon.codingtest.service.TestBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("test")
public class BoardController {
    @Autowired
    TestBoardService testBoardService;

    @GetMapping("/test_board")
    public String gotest_board(Model model) {
        List<TestBoard> testBoard= testBoardService.findAll();
        model.addAttribute("list",testBoard);
        return "test/test_board";
    }
}
