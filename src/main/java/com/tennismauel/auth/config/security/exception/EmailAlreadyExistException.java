package com.tennismauel.auth.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailAlreadyExistException extends AuthenticationException{
    public EmailAlreadyExistException(String msg) {
        super(msg);
    }
}
