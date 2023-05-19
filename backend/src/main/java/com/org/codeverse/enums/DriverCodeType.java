package com.org.codeverse.enums;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum DriverCodeType {
    @Enumerated(EnumType.STRING)
    TEST,
    @Enumerated(EnumType.STRING)
    RUN
}
