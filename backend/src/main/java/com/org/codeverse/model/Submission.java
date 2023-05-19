package com.org.codeverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.org.codeverse.enums.Language;
import com.org.codeverse.enums.SubmissionResult;
import com.org.codeverse.enums.SubmissionStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"question", "user"})
@Entity
@Table
public class Submission {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;
    @Enumerated(EnumType.STRING)
    private SubmissionResult result;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Double avgRuntime;
    private Double avgMemoryUsed;
    private Long numAccepted;
    private Long numTestCases;
    @Column(length = 10000)
    private String snippet;
    @ManyToOne(cascade = CascadeType.ALL)
    private Question question;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    private LocalDateTime createdAt;
    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}
