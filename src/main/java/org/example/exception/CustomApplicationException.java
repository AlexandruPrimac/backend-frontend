package org.example.exception;

public class CustomApplicationException extends RuntimeException {
    //This exception is designed for situations where I encounter unexpected issues in my business logic or application workflows that do not fall under database or external system-related problems.
    public CustomApplicationException(String message) {
        super(message);
    }
}
