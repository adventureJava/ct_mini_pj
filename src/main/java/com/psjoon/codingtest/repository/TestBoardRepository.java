package com.psjoon.codingtest.repository;

import com.psjoon.codingtest.entity.TestBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestBoardRepository extends JpaRepository<TestBoard, Integer> {
    // 게시글 목록을 페이징 처리해서 가져오기
    @Query(value = "SELECT * FROM test_board ORDER BY t_id DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<TestBoard> findTestBoardsWithPaging(@Param("offset") int offset, @Param("limit") int limit);

    // 전체 게시글 수 조회
    @Query(value = "SELECT COUNT(*) FROM test_board", nativeQuery = true)
    int countAllBoards();
}
