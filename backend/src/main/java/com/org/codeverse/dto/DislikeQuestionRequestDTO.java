package com.org.codeverse.dto;

import lombok.Data;

@Data
public class DislikeQuestionRequestDTO {
    private Long questionId;
    private Long userId;
}
