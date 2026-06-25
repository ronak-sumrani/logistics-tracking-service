package com.example.logisticstrackingservice.exception;

public class ConsignmentAlreadyExistsException extends ResourceAlreadyExistsException {
    public ConsignmentAlreadyExistsException(String message) {
        super(message);
    }
}