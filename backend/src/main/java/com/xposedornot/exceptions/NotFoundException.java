package com.xposedornot.exceptions;

/**
 * Thrown when the requested resource is not found (HTTP 404).
 */
public class NotFoundException extends XposedOrNotException {

    /**
     * Creates a new not found exception.
     *
     * @param message the error message
     */
    public NotFoundException(final String message) {
        super(message, 404);
    }
}
