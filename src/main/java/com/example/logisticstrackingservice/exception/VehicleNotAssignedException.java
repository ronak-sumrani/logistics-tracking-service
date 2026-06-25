package com.example.logisticstrackingservice.exception;

public class VehicleNotAssignedException extends ResourceConflictException {
    public VehicleNotAssignedException(String message) {
        super(message);
    }
}