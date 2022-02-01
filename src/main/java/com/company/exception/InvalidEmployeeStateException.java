package com.company.exception;

import org.springframework.http.HttpStatus;

public class InvalidEmployeeStateException extends ServiceRuntimeException {

    public InvalidEmployeeStateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
