package com.rabbit.mechanic.exception;

/**
 * Worker already exists exception
 */
public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}
