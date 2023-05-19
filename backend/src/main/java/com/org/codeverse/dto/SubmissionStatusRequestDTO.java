package com.org.codeverse.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmissionStatusRequestDTO {
    private Long userId;
    private List<Long> questionIds;
}
