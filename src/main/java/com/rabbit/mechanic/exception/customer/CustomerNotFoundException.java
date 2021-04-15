package com.rabbit.mechanic.exception.customer;

/**
 * Customer Not Found Exception
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
