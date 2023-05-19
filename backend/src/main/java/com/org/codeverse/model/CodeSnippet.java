package com.org.codeverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.org.codeverse.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"question", "id"})
@Entity
@Table
public class CodeSnippet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Language language;
    @Column(length = 10000)
    private String snippet;
    @ManyToOne
    private Question question;
}
