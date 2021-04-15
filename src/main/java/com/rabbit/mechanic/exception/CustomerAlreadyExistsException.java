package com.rabbit.mechanic.exception;

/**
 * Customer Already Exists Exception
 */
public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
