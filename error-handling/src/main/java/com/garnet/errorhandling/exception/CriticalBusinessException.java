package com.garnet.errorhandling.exception;

public class CriticalBusinessException extends RuntimeException {

    public CriticalBusinessException(String message) {
        super(message);
    }
}
