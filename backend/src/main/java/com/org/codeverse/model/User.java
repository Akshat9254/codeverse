package com.org.codeverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"createdAt"})
@Entity
@Table(name = "user_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String name;
    private String avatarUrl;
    private int rank;
    private String about;
    private String school;
    private String country;
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "usersLiked", cascade = CascadeType.ALL)
    private List<Question> likedQuestions = new ArrayList<>();

    @ManyToMany(mappedBy = "usersDisLiked", cascade = CascadeType.ALL)
    private List<Question> disLikedQuestions = new ArrayList<>();
    @ManyToMany(mappedBy = "usersLiked", cascade = CascadeType.ALL)
    private List<Comment> likedComments = new ArrayList<>();
    @ManyToMany(mappedBy = "usersDisLiked", cascade = CascadeType.ALL)
    private List<Comment> disLikedComments = new ArrayList<>();

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();
}
