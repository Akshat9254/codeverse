package com.org.codeverse.dto;

import com.org.codeverse.model.DifficultyCount;
import lombok.Data;

import java.util.List;

@Data
public class UserSubmissionStatResponseDTO {
    private List<DifficultyCount> totalSubmissions;
    private List<DifficultyCount> acceptedSubmissions;
}
