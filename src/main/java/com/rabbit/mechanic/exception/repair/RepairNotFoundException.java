package com.rabbit.mechanic.exception.repair;

/**
 * Repair Not Found Exception
 */
public class RepairNotFoundException extends RuntimeException {
    public RepairNotFoundException(String message) {
        super(message);
    }
}
