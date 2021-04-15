package com.rabbit.mechanic.exception.repair;

/**
 * Repair already exists exception
 */
public class RepairAlreadyExistsException extends RuntimeException {
    public RepairAlreadyExistsException(String message) {
        super(message);
    }
}
