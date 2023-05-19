package com.org.codeverse.model;

import com.org.codeverse.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DifficultyCount {
    private Difficulty difficulty;
    private long count;
}
