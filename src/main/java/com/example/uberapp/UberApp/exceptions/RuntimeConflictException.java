package com.example.uberapp.UberApp.exceptions;

public class RuntimeConflictException extends RuntimeException{

        public RuntimeConflictException() {
        }
    
        public RuntimeConflictException(String message) {
            super(message);
        }
}

