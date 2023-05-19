package com.org.codeverse.dto;

import lombok.Data;

@Data
public class LikeQuestionRequestDTO {
    private Long questionId;
    private Long userId;
}
