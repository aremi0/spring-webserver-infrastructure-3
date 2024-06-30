package com.aremi.microservizio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
