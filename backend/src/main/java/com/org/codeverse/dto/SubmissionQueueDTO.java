package com.org.codeverse.dto;

import com.org.codeverse.model.Submission;
import lombok.Data;

@Data
public class SubmissionQueueDTO {
    private Submission submission;
    private Long questionId;
}
