package com.rabbit.mechanic.exception;

/**
 * Wrong Credentials Exception
 */
public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException(String message) {
        super(message);
    }
}
