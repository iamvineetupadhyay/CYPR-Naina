package com.xposedornot.exceptions;

/**
 * Thrown when the API rate limit has been exceeded (HTTP 429).
 */
public class RateLimitException extends XposedOrNotException {

    /**
     * Creates a new rate limit exception.
     *
     * @param message the error message
     */
    public RateLimitException(final String message) {
        super(message, 429);
    }

    /**
     * Creates a new rate limit exception with a cause.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public RateLimitException(final String message, final Throwable cause) {
        super(message, 429, cause);
    }
}
