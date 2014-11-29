package com.aimlessblade.cosmos.graphics.vertex.io;

public class PLYParseError extends Exception {
    public PLYParseError(final String message) {
        super(message);
    }

    public PLYParseError(final Throwable cause) {
        super(cause);
    }

    public PLYParseError(final String message, final Throwable cause) {
        super(message, cause);
    }
}
