package com.psjoon.codingtest.service;

import com.psjoon.codingtest.entity.TestBoard;
import com.psjoon.codingtest.entity.TestRecord;
import com.psjoon.codingtest.repository.TestRecordRepository;
import com.psjoon.codingtest.util.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestRecordService {
    @Autowired
    private TestRecordRepository testRecordRepository;

    public TestRecord save(TestRecord testRecord){
        testRecordRepository.save(testRecord);
        return testRecord;
    }
    // DAO 계층에서 직접 쿼리를 실행하여 페이징 처리
    public List<TestRecord> getTestRecords(int pageNum, int listCnt, String userId) {
        int offset = (pageNum - 1) * listCnt; // 페이지 시작 인덱스
        return testRecordRepository.findTestRecordsWithPaging(offset, listCnt,userId); // 직접 SQL로 가져옴
    }

    // 전체 게시글 수 카운트
    public int getTotalCount(String userId) {
        return testRecordRepository.countAllRecords(userId); // 전체 게시글 수 조회 쿼리
    }

    // 페이징 처리된 데이터를 넘김
    public PagingUtil getPaging(int pageNum, int listCnt,String userId) {
        int totalCount = getTotalCount(userId); // 전체 게시글 수
        int pageCnt = 5; // 한 번에 보여줄 페이지 개수

        PagingUtil paging = new PagingUtil(totalCount, pageNum, listCnt, pageCnt);
        return paging;
    }

    public TestRecord findById(int id){
        return testRecordRepository.findById(id).get();
    }
}
