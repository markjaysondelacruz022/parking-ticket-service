package com.mjdc.pts.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ControllerException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    public ControllerException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
