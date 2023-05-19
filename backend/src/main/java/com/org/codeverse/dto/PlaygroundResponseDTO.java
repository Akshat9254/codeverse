package com.org.codeverse.dto;

import com.org.codeverse.enums.SubmissionResult;
import lombok.Data;

@Data
public class PlaygroundResponseDTO {
    private SubmissionResult result;
    private String output;
    private Double runtime;
}
