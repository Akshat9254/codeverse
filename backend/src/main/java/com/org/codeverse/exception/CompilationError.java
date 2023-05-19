package com.org.codeverse.exception;

import com.org.codeverse.enums.SubmissionResult;

public class CompilationError extends RuntimeException {
    private String error;
    public CompilationError(String error) {
        super(SubmissionResult.COMPILATION_ERROR.toString());
        this.error = error;
        System.out.println("Compilation Error " + error);

    }

    public String getError() {
        return error;
    }
}
