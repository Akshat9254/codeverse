package com.org.codeverse.enums;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum SubmissionStatus {
    @Enumerated(EnumType.STRING)
    QUEUED,
    @Enumerated(EnumType.STRING)
    PROCESSING,
    @Enumerated(EnumType.STRING)
    COMPLETED,
    @Enumerated(EnumType.STRING)
    ERROR
}
