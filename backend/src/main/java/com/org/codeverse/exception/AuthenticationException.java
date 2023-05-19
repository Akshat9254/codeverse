package com.org.codeverse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {
    private String message;

    public AuthenticationException(String message) {
        super(message);
    }
}
