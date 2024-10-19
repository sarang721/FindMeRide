package com.example.uberapp.UberApp.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
