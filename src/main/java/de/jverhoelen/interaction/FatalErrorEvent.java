package de.jverhoelen.interaction;

public class FatalErrorEvent {

    private String description;
    private Throwable throwable;

    public FatalErrorEvent(String description) {
        this(description, null);
    }

    public FatalErrorEvent(String description, Throwable throwable) {
        this.description = description;
        this.throwable = throwable;
    }

    public String getDescription() {
        return description;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
