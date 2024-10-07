package com.psjoon.codingtest.repository;

import com.psjoon.codingtest.entity.TestBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestBoardRepository extends JpaRepository<TestBoard, Integer> {
}
