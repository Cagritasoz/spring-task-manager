package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class WebExceptionHandler { //TO DO Fix e.get message bad responses.

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                400,
                "BAD_REQUEST",
                e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                400,
                "BAD_REQUEST",
                e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }
}

/*
This exception(HttpMessageNotReadableException) occurs when the incoming HTTP request
body cannot be converted into the expected Java object,typically due to malformed JSON,
incorrect data types (e.g., invalid enum values, date formats)
 */