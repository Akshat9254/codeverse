package com.org.codeverse.dto;

import com.org.codeverse.enums.Language;
import lombok.*;

@Data
public class SubmitQuestionRequestDTO {
    private String snippet;
    private Long questionId;
    private Long userId;
    private Language language;
}
