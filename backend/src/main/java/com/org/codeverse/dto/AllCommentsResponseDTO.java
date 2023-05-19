package com.org.codeverse.dto;


import com.org.codeverse.repository.CommentRepository;
import lombok.Data;

import java.util.List;

@Data
public class AllCommentsResponseDTO {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Boolean last;
    private List<CommentRepository.CommentDetails> comments;
}
