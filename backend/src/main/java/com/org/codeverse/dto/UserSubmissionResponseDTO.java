package com.org.codeverse.dto;

import com.org.codeverse.model.UserSubmission;
import lombok.Data;

import java.util.List;

@Data
public class UserSubmissionResponseDTO {
    List<UserSubmission> allSubmissions;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalElements;
    private Boolean last;
}
