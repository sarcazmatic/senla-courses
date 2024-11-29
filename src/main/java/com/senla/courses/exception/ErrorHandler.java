package com.senla.courses.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class ErrorHandler {

    Logger logger = Logger.getLogger("ErrorHandler");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final NotFoundException e) {
        logger.log(Level.SEVERE, "404 " + e.getMessage());
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
        logger.log(Level.SEVERE, "400 " + e.getMessage());
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
        logger.log(Level.SEVERE, "400 " + e.getMessage());
        return Error.builder()
                .timestamp(LocalDateTime.now().toString())
                .errors(Arrays.asList(e.getSuppressedFields()))
                .status(HttpStatus.BAD_REQUEST)
                .reason(Arrays.toString(e.getStackTrace()))
                .message(e.getLocalizedMessage())
                .build();
    }

}
