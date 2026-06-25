package com.example.logisticstrackingservice.exception;

public class DriverNotFoundException extends ResourceNotFoundException {
    public DriverNotFoundException(String message) {
        super(message);
    }
}