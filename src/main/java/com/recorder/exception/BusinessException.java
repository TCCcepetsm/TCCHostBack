// No seu arquivo BusinessException.java
package com.recorder.exception;

import org.springframework.http.HttpStatus; // Assumindo que você está usando o HttpStatus do Spring Framework

public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus; // Campo para armazenar o HttpStatus

    public BusinessException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Status padrão ou outro apropriado
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Status padrão ou outro apropriado
    }

    // Novo construtor para aceitar HttpStatus
    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    // Você também pode querer um construtor com mensagem, causa e httpStatus
    public BusinessException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    // Getter para httpStatus
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}