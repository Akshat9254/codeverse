package com.org.codeverse.dto;

import com.org.codeverse.enums.Difficulty;
import com.org.codeverse.model.*;
import lombok.*;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDetailsDTO {
    private Long id;
    private String title;
    private String slug;
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    private int likes;
    private int dislikes;
    private List<Hint> hints;
    private String description;
    private List<TestCase> testCases;
    private List<CodeSnippet> codeSnippets;
    private List<Solution> solutions;
    private int accepted;
    private int submissions;
    private double acceptanceRate;
    private List<Topic> topics;

}
