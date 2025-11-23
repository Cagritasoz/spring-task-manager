package com.cagritasoz.taskmanager.domain.exception;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException() {
        super("This user does not exist!");
    }
}
