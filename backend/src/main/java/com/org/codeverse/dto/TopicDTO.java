package com.org.codeverse.dto;

import com.org.codeverse.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDTO {
    private Tag tag;
    private String slug;
    private Long id;
}
