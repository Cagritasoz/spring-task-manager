package com.cagritasoz.taskmanager.domain.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("Unauthorized");
    }
}
