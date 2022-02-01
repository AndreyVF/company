package com.company.exception;

import org.springframework.http.HttpStatus;

public class ServiceRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;

    ServiceRuntimeException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    ServiceRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    ServiceRuntimeException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
