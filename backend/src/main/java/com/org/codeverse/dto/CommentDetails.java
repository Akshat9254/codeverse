package com.org.codeverse.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDetails {
    private String title;
    private String content;
    private String userName;
    private String userEmail;
    private String userAvatarUrl;
    private int likes;
    private int dislikes;
    private LocalDateTime createdAt;
}
