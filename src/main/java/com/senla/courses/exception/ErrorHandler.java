package com.senla.courses.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final NotFoundException e) {
        log.error("404 {}", e.getMessage());
        return Error.builder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(Arrays.toString(e.getStackTrace()))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleEmptyListException(final EmptyListException e) {
        log.error("404 {}", e.getMessage());
        return Error.builder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(Arrays.toString(e.getStackTrace()))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleValidationException(final ValidationException e) {
        log.error("400 {}", e.getMessage());
        return Error.builder()
                .errors(List.of(e.getClass().getName()))
                .message(e.getLocalizedMessage())
                .reason(Arrays.toString(e.getStackTrace()))
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleControllerValidationException(final MethodArgumentNotValidException e) {
        log.error("400 {}", e.getMessage());
        return Error.builder()
                .timestamp(LocalDateTime.now().toString())
                .errors(Arrays.asList(e.getSuppressedFields()))
                .status(HttpStatus.BAD_REQUEST)
                .reason(Arrays.toString(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .build();
    }

}
