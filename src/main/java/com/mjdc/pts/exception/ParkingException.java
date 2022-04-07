package com.mjdc.pts.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ParkingException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    public ParkingException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
