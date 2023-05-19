package com.org.codeverse.dto;

import com.org.codeverse.enums.Language;
import lombok.Data;

import java.util.List;

@Data
public class RunQuestionRequestDTO {
    private List<String> inputs;
    private Long questionId;
    private Language language;
    private String snippet;
}
