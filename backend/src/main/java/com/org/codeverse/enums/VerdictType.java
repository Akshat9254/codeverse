package com.org.codeverse.enums;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum VerdictType {
    @Enumerated(EnumType.STRING)
    ACCEPTED,
    @Enumerated(EnumType.STRING)
    WRONG_ANSWER,
    @Enumerated(EnumType.STRING)
    RUNTIME_ERROR,
    @Enumerated(EnumType.STRING)
    COMPILATION_ERROR
}
