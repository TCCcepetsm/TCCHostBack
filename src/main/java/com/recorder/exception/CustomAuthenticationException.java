package com.recorder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomAuthenticationException extends RuntimeException {
    private final HttpStatus status;

    // Construtor com mensagem e status
    public CustomAuthenticationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    // Construtor apenas com mensagem (usa UNAUTHORIZED por padrão)
    public CustomAuthenticationException(String message) {
        this(message, HttpStatus.UNAUTHORIZED);
    }

    // Getter para o status
    public HttpStatus getStatus() {
        return status;
    }
}