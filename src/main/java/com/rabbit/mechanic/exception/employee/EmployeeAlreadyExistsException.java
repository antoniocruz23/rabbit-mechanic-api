package com.rabbit.mechanic.exception.employee;

/**
 * Employee already exists exception
 */
public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}
