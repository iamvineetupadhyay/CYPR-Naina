package com.xposedornot.exceptions;

/**
 * Thrown when authentication fails (HTTP 401/403).
 */
public class AuthenticationException extends XposedOrNotException {

    /**
     * Creates a new authentication exception.
     *
     * @param message    the error message
     * @param statusCode the HTTP status code (401 or 403)
     */
    public AuthenticationException(final String message, final int statusCode) {
        super(message, statusCode);
    }

    /**
     * Creates a new authentication exception with default 401 status.
     *
     * @param message the error message
     */
    public AuthenticationException(final String message) {
        super(message, 401);
    }
}
