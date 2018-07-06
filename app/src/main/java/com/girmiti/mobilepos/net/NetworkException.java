package com.girmiti.mobilepos.net;

public class NetworkException extends Exception {

    private static final long serialVersionUID = 1L;

    public NetworkException() {
        super();
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructs a new {@code Exception} with the current stack trace and the
     * specified cause.
     *
     * @param throwable the cause of this exception.
     */
    public NetworkException(Throwable throwable) {
        super(throwable);
    }
}
