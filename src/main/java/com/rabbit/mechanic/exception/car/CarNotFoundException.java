package com.rabbit.mechanic.exception.car;

/**
 * Car Not Found Exception
 */
public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) {
        super(message);
    }
}
