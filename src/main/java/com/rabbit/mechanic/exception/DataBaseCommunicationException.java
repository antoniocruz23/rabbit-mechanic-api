package com.rabbit.mechanic.exception;

/**
 * DataBase Communication Exception
 */
public class DataBaseCommunicationException extends RuntimeException {
    public DataBaseCommunicationException(String message, Throwable e) {
        super(message, e);
    }
}
