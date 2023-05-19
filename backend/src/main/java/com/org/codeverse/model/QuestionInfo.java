package com.org.codeverse.model;

import com.org.codeverse.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInfo {
    private Long id;
    private String title;
    private String slug;
    private Difficulty difficulty;
    private Float acceptanceRate;
    private Boolean solved;
}
