package com.rabbit.mechanic.exception.customer;

/**
 * Customer Already Exists Exception
 */
public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
