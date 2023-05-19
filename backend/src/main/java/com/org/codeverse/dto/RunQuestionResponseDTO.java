package com.org.codeverse.dto;

import lombok.Data;

import java.util.List;

@Data
public class RunQuestionResponseDTO {
    private String result;
    private List<String> verdicts;
    private List<String> outputs;
    private List<String> expectedOutputs;
    private List<String> stdout;
    private Double runtime;
}
