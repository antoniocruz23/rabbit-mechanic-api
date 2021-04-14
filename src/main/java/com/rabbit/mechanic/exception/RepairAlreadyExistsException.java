package com.rabbit.mechanic.exception;

/**
 * Repair already exists exception
 */
public class RepairAlreadyExistsException extends RuntimeException {
    public RepairAlreadyExistsException(String message) {
        super(message);
    }
}
