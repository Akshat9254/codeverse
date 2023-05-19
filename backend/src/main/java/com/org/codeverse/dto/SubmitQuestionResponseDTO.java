package com.org.codeverse.dto;

import com.org.codeverse.enums.SubmissionResult;
import lombok.Data;

@Data
public class SubmitQuestionResponseDTO {
    private String id;
    private SubmissionResult submissionResult;
    private long numAccepted;
    private long numTestCases;
    private double avgRuntime;
    private double avgMemoryUsed;

}
