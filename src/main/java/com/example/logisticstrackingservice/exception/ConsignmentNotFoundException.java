package com.example.logisticstrackingservice.exception;

public class ConsignmentNotFoundException extends ResourceNotFoundException {
    public ConsignmentNotFoundException(String message) {
        super(message);
    }
}