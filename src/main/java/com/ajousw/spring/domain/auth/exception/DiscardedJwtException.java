package com.ajousw.spring.domain.auth.exception;

public class DiscardedJwtException extends RuntimeException {
    public DiscardedJwtException(String message) {
        super(message);
    }

    public DiscardedJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
