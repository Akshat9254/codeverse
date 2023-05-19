package com.org.codeverse.model;

import com.org.codeverse.enums.Language;
import com.org.codeverse.enums.SubmissionResult;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSubmission {
    private String id;
    private String questionTitle;
    private String questionSlug;
    private Object date;
    private SubmissionResult result;
    private String snippet;
    private Double avgRuntime;
    private Double avgMemoryUsage;
    private Language language;

    public UserSubmission(String id,
                          String questionTitle,
                          String questionSlug,
                          SubmissionResult result,
                          Object date,
                          String snippet,
                          Double avgRuntime,
                          Double avgMemoryUsage,
                          Language language) {
        this.id = id;
        this.questionTitle = questionTitle;
        this.questionSlug = questionSlug;
        this.date = date;
        this.result = result;
        this.snippet = snippet;
        this.avgRuntime = avgRuntime;
        this.avgMemoryUsage = avgMemoryUsage;
        this.language = language;
    }
}
