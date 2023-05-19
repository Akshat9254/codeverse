package com.org.codeverse.dto;

import com.org.codeverse.repository.QuestionRepository;
import lombok.Data;

import java.util.List;

@Data
public class AllQuestionResponseDTO {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Boolean last;
    private List<QuestionRepository.QuestionInfo> allQuestions;
}
