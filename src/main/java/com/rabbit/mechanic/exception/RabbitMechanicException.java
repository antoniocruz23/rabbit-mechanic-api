package com.rabbit.mechanic.exception;

/**
 * Global RabbitMechanic exception
 */
public class RabbitMechanicException extends RuntimeException{
    public RabbitMechanicException() {
    }

    public RabbitMechanicException(String message) {
        super(message);
    }

    public RabbitMechanicException(String message, Throwable cause) {
        super(message, cause);
    }

    public RabbitMechanicException(Throwable cause) {
        super(cause);
    }

    public RabbitMechanicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
