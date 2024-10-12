package com.psjoon.codingtest.repository;

import com.psjoon.codingtest.entity.TestBoard;
import com.psjoon.codingtest.entity.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRecordRepository extends JpaRepository<TestRecord, Integer> {
    // 게시글 목록을 페이징 처리해서 가져오기
    @Query(value = "SELECT * FROM test_record where id = :userId ORDER BY t_id DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<TestRecord> findTestRecordsWithPaging(@Param("offset") int offset, @Param("limit") int limit,@Param("userId") String userId);

    // 전체 게시글 수 조회
    @Query(value = "SELECT COUNT(*) FROM test_record where id = :userId", nativeQuery = true)
    int countAllRecords(@Param("userId") String userId);
}
