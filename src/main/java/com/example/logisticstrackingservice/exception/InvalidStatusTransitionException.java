package com.example.logisticstrackingservice.exception;

public class InvalidStatusTransitionException extends ResourceConflictException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}