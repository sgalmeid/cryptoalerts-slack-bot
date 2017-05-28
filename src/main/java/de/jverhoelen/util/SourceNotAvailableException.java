package de.jverhoelen.util;

public class SourceNotAvailableException extends Exception {

    public SourceNotAvailableException(String message) {
        super(message);
    }

    public SourceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
