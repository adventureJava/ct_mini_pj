package com.psjoon.codingtest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CodeRequest {
    private String code;
    private String language;

    @JsonProperty("tId")
    private Integer tId;
}
