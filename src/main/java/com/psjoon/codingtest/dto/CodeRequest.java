package com.psjoon.codingtest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CodeRequest {
    private String code;
    private String language;
}
