package com.org.codeverse.dto;

import com.org.codeverse.enums.Language;
import lombok.Data;

@Data
public class PlaygroundRequestDTO {
    private Language language;
    private String input;
    private String code;
}
