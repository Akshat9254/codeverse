package com.org.codeverse.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum Language {
    @Enumerated(EnumType.STRING)
    JAVA,
    @Enumerated(EnumType.STRING)
    CPP,
    @Enumerated(EnumType.STRING)
    PYTHON
}
