package com.rabbit.mechanic.exception.employee;

/**
 * Employee Not Found Exception
 */
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
