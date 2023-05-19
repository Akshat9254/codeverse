package com.org.codeverse.dto;

import com.org.codeverse.model.SubmissionDateCount;
import lombok.Data;

import java.util.List;

@Data
public class UserSubmissionsByDateResponseDTO {
    private List<SubmissionDateCount> submissionDateCounts;
}
