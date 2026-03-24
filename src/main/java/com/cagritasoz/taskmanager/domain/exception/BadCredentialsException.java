package com.cagritasoz.taskmanager.domain.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Invalid email or password!");
    }
}
