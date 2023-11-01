package com.ajousw.spring.domain.timetable.exception;

public class EverytimeParsingException extends RuntimeException {
    public EverytimeParsingException() {
    }

    public EverytimeParsingException(String message) {
        super(message);
    }

    public EverytimeParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
