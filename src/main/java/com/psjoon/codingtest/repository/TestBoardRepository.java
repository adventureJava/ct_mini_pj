package com.psjoon.codingtest.repository;

import com.psjoon.codingtest.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestBoardRepository extends JpaRepository<Authority, Integer> {
}
