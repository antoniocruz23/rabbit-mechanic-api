package com.rabbit.mechanic.exception;

/**
 * Repair Not Found Exception
 */
public class RepairNotFoundException extends RuntimeException {
    public RepairNotFoundException(String message) {
        super(message);
    }
}
