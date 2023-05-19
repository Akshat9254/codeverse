package com.org.codeverse.enums;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum SubmissionResult {
    @Enumerated(EnumType.STRING)
    ACCEPTED,
    @Enumerated(EnumType.STRING)
    WRONG_ANSWER,
    @Enumerated(EnumType.STRING)
    TIME_LIMIT_EXCEEDED,
    @Enumerated(EnumType.STRING)
    MEMORY_LIMIT_EXCEEDED,
    @Enumerated(EnumType.STRING)
    COMPILATION_ERROR,
    @Enumerated(EnumType.STRING)
    RUNTIME_ERROR,
    @Enumerated(EnumType.STRING)
    SUCCESSFUL,
    @Enumerated(EnumType.STRING)
    SOMETHING_WENT_WRONG
}
