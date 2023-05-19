package com.org.codeverse.exception;


import com.org.codeverse.enums.SubmissionResult;

public class TimeLimitExceededException extends RuntimeException{
    public TimeLimitExceededException() {
        super(SubmissionResult.TIME_LIMIT_EXCEEDED.toString());
    }
}
