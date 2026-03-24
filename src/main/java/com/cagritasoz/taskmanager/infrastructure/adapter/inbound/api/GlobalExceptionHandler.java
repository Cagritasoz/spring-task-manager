package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.domain.exception.BadCredentialsException;
import com.cagritasoz.taskmanager.domain.exception.EmailAlreadyExistsException;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler { //For domain exceptions!

    //Exceptions thrown in the security filter can not reach here unless manually
    //forwarded here with a HandlerExceptionResolver.


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                409,
                e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                401,
                e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    }


}
