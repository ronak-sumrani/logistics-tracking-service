package com.example.logisticstrackingservice.exception;

public class AllProvidersFailedException extends RuntimeException {
    public AllProvidersFailedException(String message) {
        super(message);
    }
}