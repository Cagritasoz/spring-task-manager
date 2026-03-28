package com.cagritasoz.taskmanager.domain.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Not allowed to access resource");
    }
}
