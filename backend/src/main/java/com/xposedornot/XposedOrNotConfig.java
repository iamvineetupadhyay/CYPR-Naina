package com.xposedornot;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for the XposedOrNot client.
 *
 * <p>Use {@link XposedOrNot.Builder} to construct instances.</p>
 */
public final class XposedOrNotConfig {

    /** Default base URL for the free API. */
    public static final String DEFAULT_BASE_URL = "https://api.xposedornot.com";

    /** Base URL for the Plus (commercial) API. */
    public static final String PLUS_BASE_URL = "https://plus-api.xposedornot.com";

    /** Base URL for the password exposure API. */
    public static final String PASSWORD_BASE_URL = "https://passwords.xposedornot.com/api";

    /** Default HTTP request timeout. */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    /** Default maximum number of retries on 429 responses. */
    public static final int DEFAULT_MAX_RETRIES = 3;

    private final String baseUrl;
    private final String plusBaseUrl;
    private final String passwordBaseUrl;
    private final Duration timeout;
    private final int maxRetries;
    private final String apiKey;
    private final Map<String, String> customHeaders;

    XposedOrNotConfig(final String baseUrl,
                      final String plusBaseUrl,
                      final String passwordBaseUrl,
                      final Duration timeout,
                      final int maxRetries,
                      final String apiKey,
                      final Map<String, String> customHeaders) {
        this.baseUrl = baseUrl;
        this.plusBaseUrl = plusBaseUrl;
        this.passwordBaseUrl = passwordBaseUrl;
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.apiKey = apiKey;
        this.customHeaders = Collections.unmodifiableMap(new HashMap<>(customHeaders));
    }

    /**
     * Returns the base URL for the free API.
     *
     * @return the free API base URL
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Returns the base URL for the Plus API.
     *
     * @return the Plus API base URL
     */
    public String getPlusBaseUrl() {
        return plusBaseUrl;
    }

    /**
     * Returns the base URL for the password API.
     *
     * @return the password API base URL
     */
    public String getPasswordBaseUrl() {
        return passwordBaseUrl;
    }

    /**
     * Returns the HTTP request timeout.
     *
     * @return the timeout duration
     */
    public Duration getTimeout() {
        return timeout;
    }

    /**
     * Returns the maximum number of retries on 429 responses.
     *
     * @return the max retry count
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Returns the API key, or null if not set.
     *
     * @return the API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Returns whether an API key has been configured (i.e., Plus API mode).
     *
     * @return true if an API key is set
     */
    public boolean hasApiKey() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    /**
     * Returns any custom headers configured.
     *
     * @return an unmodifiable map of custom headers
     */
    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }
}
