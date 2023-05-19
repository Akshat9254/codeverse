package com.org.codeverse.dto;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private Long userId;
    private Long questionId;
    private String title;
    private String content;
}
