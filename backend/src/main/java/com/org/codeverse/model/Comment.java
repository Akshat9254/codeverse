package com.org.codeverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"question", "id", "user"})
@Entity
@Table
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Question question;
    private String title;
    @Column(length = 10000)
    private String content;
    @ManyToMany
    private List<User> usersLiked = new ArrayList<>();
    @ManyToMany
    private List<User> usersDisLiked = new ArrayList<>();
    private LocalDateTime createdAt;
    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
    private LocalDateTime updatedAt;
}
