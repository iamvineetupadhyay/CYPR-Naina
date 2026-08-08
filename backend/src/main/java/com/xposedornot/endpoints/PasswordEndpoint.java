package com.xposedornot.endpoints;

import com.xposedornot.HttpClientWrapper;
import com.xposedornot.XposedOrNotConfig;
import com.xposedornot.exceptions.NotFoundException;
import com.xposedornot.exceptions.ValidationException;
import com.xposedornot.exceptions.XposedOrNotException;
import com.xposedornot.models.PasswordCheckResponse;
import com.xposedornot.utils.KeccakHasher;

/**
 * Endpoint for anonymous password exposure checks.
 *
 * <p>Passwords are hashed locally using original Keccak-512. Only the first 10
 * hex characters of the hash are sent to the API, preserving privacy through
 * a k-anonymity approach.</p>
 */
public class PasswordEndpoint {

    private final HttpClientWrapper httpClient;
    private final XposedOrNotConfig config;

    /**
     * Creates a new password endpoint.
     *
     * @param httpClient the HTTP client wrapper
     * @param config     the client configuration
     */
    public PasswordEndpoint(final HttpClientWrapper httpClient, final XposedOrNotConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    /**
     * Checks if a password has been exposed in any known data breach.
     *
     * <p>The password is hashed locally with Keccak-512 and only the first 10 hex
     * characters of the hash are transmitted to the API.</p>
     *
     * @param password the password to check
     * @return the password check response
     * @throws XposedOrNotException if the request fails or the password is invalid
     */
    public PasswordCheckResponse check(final String password) throws XposedOrNotException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password must not be null or empty");
        }
        final String hashPrefix = KeccakHasher.hashPrefix(password);
        final String url = config.getPasswordBaseUrl() + "/v1/pass/anon/" + hashPrefix;
        try {
            return httpClient.get(url, PasswordCheckResponse.class, false);
        } catch (NotFoundException e) {
            // 404 means password hash prefix not found — password is not exposed
            return PasswordCheckResponse.notFound(hashPrefix);
        }
    }

    /**
     * Checks if a password has been exposed, using a pre-computed Keccak-512 hash prefix.
     *
     * @param hashPrefix the first 10 hex characters of the Keccak-512 hash
     * @return the password check response
     * @throws XposedOrNotException if the request fails or the hash prefix is invalid
     */
    public PasswordCheckResponse checkByHashPrefix(final String hashPrefix) throws XposedOrNotException {
        if (hashPrefix == null || hashPrefix.length() != 10) {
            throw new ValidationException("Hash prefix must be exactly 10 hex characters");
        }
        if (!hashPrefix.matches("[0-9a-fA-F]{10}")) {
            throw new ValidationException("Hash prefix must contain only hexadecimal characters");
        }
        final String url = config.getPasswordBaseUrl() + "/v1/pass/anon/" + hashPrefix.toLowerCase();
        return httpClient.get(url, PasswordCheckResponse.class, false);
    }
}
