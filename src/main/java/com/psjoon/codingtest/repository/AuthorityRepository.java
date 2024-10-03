package com.psjoon.codingtest.repository;

import com.psjoon.codingtest.entity.Authority;
import com.psjoon.codingtest.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
