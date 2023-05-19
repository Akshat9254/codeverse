package com.org.codeverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.org.codeverse.enums.DriverCodeType;
import com.org.codeverse.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"question", "id"})
@Entity
@Table
public class DriverCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Language language;
    @Column(length = 8000)
    private String code;
    @Enumerated(EnumType.STRING)
    private DriverCodeType type;
    @ManyToOne
    private Question question;
}
