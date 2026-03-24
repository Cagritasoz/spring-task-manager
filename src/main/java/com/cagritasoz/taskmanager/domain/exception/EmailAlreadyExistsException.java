package com.cagritasoz.taskmanager.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email already in use");
    }
}
