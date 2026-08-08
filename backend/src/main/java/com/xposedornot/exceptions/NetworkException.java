package com.xposedornot.exceptions;

/**
 * Thrown when a network error occurs (connection timeout, DNS failure, etc.).
 */
public class NetworkException extends XposedOrNotException {

    /**
     * Creates a new network exception.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public NetworkException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new network exception.
     *
     * @param message the error message
     */
    public NetworkException(final String message) {
        super(message);
    }
}
