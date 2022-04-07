package com.mjdc.pts.exception;

import com.mjdc.pts.dto.ResponseDto;
import com.mjdc.pts.util.DateUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<ResponseDto> handleControllerException(final ControllerException ex,
                                                                 final WebRequest request) {
        return new ResponseEntity<>(ResponseDto.builder()
            .timestamp(DateUtil.getCurrentDateTime())
            .message(ex.getMessage())
            .build(), ex.getHttpStatus());
    }

    @ExceptionHandler(ParkingException.class)
    public ResponseEntity<ResponseDto> handleParkingException(final ParkingException ex,
                                                                 final WebRequest request) {
        return new ResponseEntity<>(ResponseDto.builder()
            .timestamp(DateUtil.getCurrentDateTime())
            .message(ex.getMessage())
            .build(), ex.getHttpStatus());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseDto> handleResponseStatusException(final ResponseStatusException ex,
                                                                     final WebRequest request) {

        final AtomicReference<String> messageAr = new AtomicReference<>(ex.getMessage());
        Optional.ofNullable(ex.getReason()).ifPresent(messageAr::set);

        return new ResponseEntity<>(ResponseDto.builder()
            .timestamp(DateUtil.getCurrentDateTime())
            .message(messageAr.get())
            .build(), ex.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto> handleDataIntegrityViolationException(final DataIntegrityViolationException ex,
                                                                             final WebRequest request) {

        final AtomicReference<String> messageAr = new AtomicReference<>(ex.getMessage());
        Optional.ofNullable(ex.getCause()).flatMap(c -> Optional.ofNullable(c.getCause()))
            .flatMap(c -> Optional.ofNullable(c.getMessage())).ifPresent(messageAr::set);

        return new ResponseEntity<>(ResponseDto.builder()
            .timestamp(DateUtil.getCurrentDateTime())
            .message(messageAr.get())
            .build(), HttpStatus.BAD_REQUEST);
    }

    /**Handles @Valid annotation
     * in Controller.**/
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final Map<String, String> errors = new ConcurrentHashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(ResponseDto.builder()
            .timestamp(DateUtil.getCurrentDateTime())
            .message("Found violation")
            .data(errors)
            .build(), headers, status);
    }

    public static Exception getExceptionThrow(final Exception ex, final String message) {
        final Exception throwException;
        if (ex instanceof ResponseStatusException
            || ex instanceof DataIntegrityViolationException
            || ex instanceof ParkingException) {
            throwException = ex;
        } else {
            throwException = new ControllerException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return throwException;
    }
}
