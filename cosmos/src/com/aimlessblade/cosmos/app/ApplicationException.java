package com.aimlessblade.cosmos.app;

class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
