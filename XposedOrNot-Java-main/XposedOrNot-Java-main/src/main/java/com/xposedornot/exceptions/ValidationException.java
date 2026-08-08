package com.xposedornot.exceptions;

/**
 * Thrown when input validation fails (e.g., invalid email format).
 */
public class ValidationException extends XposedOrNotException {

    /**
     * Creates a new validation exception.
     *
     * @param message the error message
     */
    public ValidationException(final String message) {
        super(message);
    }
}
