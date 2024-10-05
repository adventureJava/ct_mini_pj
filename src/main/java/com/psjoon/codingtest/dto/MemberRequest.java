package com.psjoon.codingtest.dto;

import com.psjoon.codingtest.entity.Authority;
import com.psjoon.codingtest.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MemberRequest {
    private Member member;
    private Authority authority;
}
