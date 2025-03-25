package com.example.bookstore.exception;

// Custom exception class to handle cases where a requested resource is not found.
public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L; // Ensures class consistency during serialization.

    // Constructor that accepts a custom error message and passes it to the superclass (RuntimeException).
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
