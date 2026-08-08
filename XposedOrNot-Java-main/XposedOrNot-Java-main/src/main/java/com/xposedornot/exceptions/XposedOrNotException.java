package com.xposedornot.exceptions;

/**
 * Base checked exception for all XposedOrNot API errors.
 */
public class XposedOrNotException extends Exception {

    private final int statusCode;

    /**
     * Creates a new exception with the given message.
     *
     * @param message the error message
     */
    public XposedOrNotException(final String message) {
        super(message);
        this.statusCode = 0;
    }

    /**
     * Creates a new exception with the given message and status code.
     *
     * @param message    the error message
     * @param statusCode the HTTP status code
     */
    public XposedOrNotException(final String message, final int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Creates a new exception with the given message and cause.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public XposedOrNotException(final String message, final Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
    }

    /**
     * Creates a new exception with the given message, status code, and cause.
     *
     * @param message    the error message
     * @param statusCode the HTTP status code
     * @param cause      the underlying cause
     */
    public XposedOrNotException(final String message, final int statusCode, final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP status code associated with this error, or 0 if not applicable.
     *
     * @return the HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}
