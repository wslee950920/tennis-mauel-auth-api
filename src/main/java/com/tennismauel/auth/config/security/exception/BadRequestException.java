package com.tennismauel.auth.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class BadRequestException extends AuthenticationException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
