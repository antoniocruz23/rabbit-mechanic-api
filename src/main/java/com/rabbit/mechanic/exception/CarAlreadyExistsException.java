package com.rabbit.mechanic.exception;

/**
 * Car already exists exception
 */
public class CarAlreadyExistsException extends RuntimeException {
    public CarAlreadyExistsException(String message) {
        super(message);
    }
}
