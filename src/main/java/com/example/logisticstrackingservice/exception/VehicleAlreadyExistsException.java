package com.example.logisticstrackingservice.exception;

public class VehicleAlreadyExistsException extends ResourceAlreadyExistsException {
    public VehicleAlreadyExistsException(String message) {
        super(message);
    }
}