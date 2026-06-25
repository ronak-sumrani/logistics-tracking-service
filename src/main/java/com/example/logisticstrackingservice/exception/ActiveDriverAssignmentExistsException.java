package com.example.logisticstrackingservice.exception;

public class ActiveDriverAssignmentExistsException extends ResourceAlreadyExistsException {
    public ActiveDriverAssignmentExistsException(String message) {
        super(message);
    }
}