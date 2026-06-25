package com.example.logisticstrackingservice.exception;

public class DriverAlreadyExistsException extends ResourceAlreadyExistsException {
    public DriverAlreadyExistsException(String message) {
        super(message);
    }
}