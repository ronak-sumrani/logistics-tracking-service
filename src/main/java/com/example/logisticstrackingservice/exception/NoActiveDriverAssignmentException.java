package com.example.logisticstrackingservice.exception;

public class NoActiveDriverAssignmentException extends ResourceConflictException {
    public NoActiveDriverAssignmentException(String message) {
        super(message);
    }
}