package com.xposedornot;

import com.xposedornot.endpoints.BreachesEndpoint;
import com.xposedornot.endpoints.EmailEndpoint;
import com.xposedornot.endpoints.PasswordEndpoint;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Main client for the XposedOrNot data breach checking API.
 *
 * <p>Use the {@link Builder} to create and configure instances:</p>
 * <pre>{@code
 * // Free API usage
 * try (XposedOrNot client = XposedOrNot.builder().build()) {
 *     EmailBreachResponse response = client.email().check("user@example.com");
 * }
 *
 * // Plus API usage with API key
 * try (XposedOrNot client = XposedOrNot.builder()
 *         .apiKey("your-api-key")
 *         .build()) {
 *     EmailBreachDetailedResponse response = client.email().checkDetailed("user@example.com");
 * }
 * }</pre>
 *
 * <p>Implements {@link AutoCloseable} for proper resource management.</p>
 */
public class XposedOrNot implements AutoCloseable {

    private final XposedOrNotConfig config;
    private final HttpClientWrapper httpClient;
    private final EmailEndpoint emailEndpoint;
    private final BreachesEndpoint breachesEndpoint;
    private final PasswordEndpoint passwordEndpoint;

    private XposedOrNot(final XposedOrNotConfig config) {
        this.config = config;
        this.httpClient = new HttpClientWrapper(config);
        this.emailEndpoint = new EmailEndpoint(httpClient, config);
        this.breachesEndpoint = new BreachesEndpoint(httpClient, config);
        this.passwordEndpoint = new PasswordEndpoint(httpClient, config);
    }

    /**
     * Creates a new builder for configuring and constructing an {@link XposedOrNot} client.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the email endpoint for checking email breaches and analytics.
     *
     * @return the email endpoint
     */
    public EmailEndpoint email() {
        return emailEndpoint;
    }

    /**
     * Returns the breaches endpoint for listing known data breaches.
     *
     * @return the breaches endpoint
     */
    public BreachesEndpoint breaches() {
        return breachesEndpoint;
    }

    /**
     * Returns the password endpoint for anonymous password exposure checks.
     *
     * @return the password endpoint
     */
    public PasswordEndpoint password() {
        return passwordEndpoint;
    }

    /**
     * Returns the current configuration.
     *
     * @return the configuration
     */
    public XposedOrNotConfig getConfig() {
        return config;
    }

    @Override
    public void close() {
        httpClient.close();
    }

    /**
     * Builder for constructing {@link XposedOrNot} client instances.
     */
    public static class Builder {

        private String baseUrl = XposedOrNotConfig.DEFAULT_BASE_URL;
        private String plusBaseUrl = XposedOrNotConfig.PLUS_BASE_URL;
        private String passwordBaseUrl = XposedOrNotConfig.PASSWORD_BASE_URL;
        private Duration timeout = XposedOrNotConfig.DEFAULT_TIMEOUT;
        private int maxRetries = XposedOrNotConfig.DEFAULT_MAX_RETRIES;
        private String apiKey;
        private boolean allowInsecure = false;
        private final Map<String, String> customHeaders = new HashMap<>();

        Builder() {
        }

        /**
         * Sets the base URL for the free API.
         *
         * @param baseUrl the base URL
         * @return this builder
         */
        public Builder baseUrl(final String baseUrl) {
            this.baseUrl = stripTrailingSlash(baseUrl);
            return this;
        }

        /**
         * Sets the base URL for the Plus API.
         *
         * @param plusBaseUrl the Plus API base URL
         * @return this builder
         */
        public Builder plusBaseUrl(final String plusBaseUrl) {
            this.plusBaseUrl = stripTrailingSlash(plusBaseUrl);
            return this;
        }

        /**
         * Sets the base URL for the password API.
         *
         * @param passwordBaseUrl the password API base URL
         * @return this builder
         */
        public Builder passwordBaseUrl(final String passwordBaseUrl) {
            this.passwordBaseUrl = stripTrailingSlash(passwordBaseUrl);
            return this;
        }

        /**
         * Sets the HTTP request timeout.
         *
         * @param timeout the timeout duration
         * @return this builder
         */
        public Builder timeout(final Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Sets the maximum number of retries on 429 responses.
         *
         * @param maxRetries the maximum retry count (must be non-negative)
         * @return this builder
         */
        public Builder maxRetries(final int maxRetries) {
            if (maxRetries < 0) {
                throw new IllegalArgumentException("maxRetries must be non-negative");
            }
            this.maxRetries = maxRetries;
            return this;
        }

        /**
         * Sets the API key for Plus API access.
         *
         * <p>When set, requests to the Plus API will include the {@code x-api-key} header,
         * and client-side rate limiting will be disabled.</p>
         *
         * @param apiKey the API key
         * @return this builder
         */
        public Builder apiKey(final String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Allows insecure (non-HTTPS) base URLs. Intended for testing only.
         *
         * @param allowInsecure whether to allow insecure URLs
         * @return this builder
         */
        public Builder allowInsecure(final boolean allowInsecure) {
            this.allowInsecure = allowInsecure;
            return this;
        }

        /**
         * Adds a custom header to all requests.
         *
         * @param name  the header name
         * @param value the header value
         * @return this builder
         */
        public Builder header(final String name, final String value) {
            this.customHeaders.put(name, value);
            return this;
        }

        /**
         * Builds and returns a new {@link XposedOrNot} client.
         *
         * @return the configured client instance
         */
        public XposedOrNot build() {
            if (!allowInsecure) {
                validateHttps(baseUrl, "baseUrl");
                validateHttps(plusBaseUrl, "plusBaseUrl");
                validateHttps(passwordBaseUrl, "passwordBaseUrl");
            }
            final XposedOrNotConfig config = new XposedOrNotConfig(
                    baseUrl, plusBaseUrl, passwordBaseUrl,
                    timeout, maxRetries, apiKey, customHeaders);
            return new XposedOrNot(config);
        }

        private void validateHttps(final String url, final String name) {
            if (url == null) {
                throw new IllegalArgumentException(name + " must not be null");
            }
            if (!url.toLowerCase().startsWith("https://")) {
                throw new IllegalArgumentException(name + " must use HTTPS scheme");
            }
        }

        private static String stripTrailingSlash(final String url) {
            if (url != null && url.endsWith("/")) {
                return url.substring(0, url.length() - 1);
            }
            return url;
        }
    }
}
