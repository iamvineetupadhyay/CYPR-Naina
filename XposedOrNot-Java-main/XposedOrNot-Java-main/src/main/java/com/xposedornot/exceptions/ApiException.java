package com.xposedornot.exceptions;

/**
 * Thrown when the API returns an unexpected error response.
 */
public class ApiException extends XposedOrNotException {

    private final String responseBody;

    /**
     * Creates a new API exception.
     *
     * @param message      the error message
     * @param statusCode   the HTTP status code
     * @param responseBody the raw response body
     */
    public ApiException(final String message, final int statusCode, final String responseBody) {
        super(message, statusCode);
        this.responseBody = responseBody;
    }

    /**
     * Creates a new API exception.
     *
     * @param message    the error message
     * @param statusCode the HTTP status code
     */
    public ApiException(final String message, final int statusCode) {
        super(message, statusCode);
        this.responseBody = null;
    }

    /**
     * Returns the raw response body, if available.
     *
     * @return the response body or null
     */
    public String getResponseBody() {
        return responseBody;
    }
}
