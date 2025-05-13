package com.exception;

public class IventoryDepletedException extends RuntimeException{
    public IventoryDepletedException(String message) {
        super(message);
    }
}
