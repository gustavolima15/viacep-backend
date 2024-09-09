package com.backendufbaendereco.demo.Exeption;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}