package com.psjoon.codingtest.service;

import com.psjoon.codingtest.entity.TestBoard;
import com.psjoon.codingtest.repository.TestBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestBoardService {
    @Autowired
    private TestBoardRepository testBoardRepository;

    public List<TestBoard> findAll(){
        return testBoardRepository.findAll();
    }

    public TestBoard findById(int id){
        return testBoardRepository.findById(id).get();
    }

}
