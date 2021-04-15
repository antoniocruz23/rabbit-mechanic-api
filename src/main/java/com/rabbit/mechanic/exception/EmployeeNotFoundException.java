package com.rabbit.mechanic.exception;

/**
 * Worker Not Found Exception
 */
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
