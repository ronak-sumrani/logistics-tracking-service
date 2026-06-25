package com.example.logisticstrackingservice.exception;

public class VehicleNotFoundException extends ResourceNotFoundException {
    public VehicleNotFoundException(String message) {
        super(message);
    }
}