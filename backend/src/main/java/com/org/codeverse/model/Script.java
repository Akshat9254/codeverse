package com.org.codeverse.model;

import com.org.codeverse.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Script {
    @Id
    private Long id;
    @Column(length = 4000)
    private String script;
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private Language language;
}
