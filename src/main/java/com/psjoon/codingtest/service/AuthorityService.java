package com.psjoon.codingtest.service;

import com.psjoon.codingtest.entity.Authority;
import com.psjoon.codingtest.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    public Authority save(Authority authority) {
        authorityRepository.save(authority);
        return authority;
    }
}
