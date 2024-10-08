package com.psjoon.codingtest.service;

import com.psjoon.codingtest.entity.TestBoard;
import com.psjoon.codingtest.repository.TestBoardRepository;
import com.psjoon.codingtest.util.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestBoardService {
    @Autowired
    private TestBoardRepository testBoardRepository;

    // DAO 계층에서 직접 쿼리를 실행하여 페이징 처리
    public List<TestBoard> getTestBoards(int pageNum, int listCnt) {
        int offset = (pageNum - 1) * listCnt; // 페이지 시작 인덱스
        return testBoardRepository.findTestBoardsWithPaging(offset, listCnt); // 직접 SQL로 가져옴
    }

    // 전체 게시글 수 카운트
    public int getTotalCount() {
        return testBoardRepository.countAllBoards(); // 전체 게시글 수 조회 쿼리
    }

    // 페이징 처리된 데이터를 넘김
    public PagingUtil getPaging(int pageNum, int listCnt) {
        int totalCount = getTotalCount(); // 전체 게시글 수
        int pageCnt = 5; // 한 번에 보여줄 페이지 개수

        PagingUtil paging = new PagingUtil(totalCount, pageNum, listCnt, pageCnt);
        return paging;
    }

    public TestBoard findById(int id){
        return testBoardRepository.findById(id).get();
    }
}
