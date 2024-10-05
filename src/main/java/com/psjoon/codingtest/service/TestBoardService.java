package com.psjoon.codingtest.service;

import com.psjoon.codingtest.repository.TestBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBoardService {
    @Autowired
    private TestBoardRepository testBoardRepository;
}
