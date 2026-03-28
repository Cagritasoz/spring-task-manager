package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.handler;

import com.cagritasoz.taskmanager.domain.exception.BadCredentialsException;
import com.cagritasoz.taskmanager.domain.exception.EmailAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class DomainExceptionHandler { //For domain exceptions!

    //Exceptions thrown in the security filter can not reach here unless manually
    //unless forwarded here with a HandlerExceptionResolver.


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                409,
                "CONFLICT",
                e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                401,
                "UNAUTHORIZED",
                e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                403,
                "FORBIDDEN",
                e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                404,
                "NOT_FOUND",
                e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

    }


}
