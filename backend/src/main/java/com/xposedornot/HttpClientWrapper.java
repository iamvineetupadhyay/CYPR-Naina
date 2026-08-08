package com.xposedornot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xposedornot.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * Internal HTTP client wrapper that handles rate limiting, retries with
 * exponential backoff, and response status code mapping to exceptions.
 */
public class HttpClientWrapper implements AutoCloseable {

    private final HttpClient httpClient;
    private final XposedOrNotConfig config;
    private final ObjectMapper objectMapper;
    private final Object rateLimitLock = new Object();
    private long lastRequestTimeMs = 0;

    /** Minimum interval between requests for rate limiting (1 second). */
    private static final long RATE_LIMIT_INTERVAL_MS = 1000L;

    /**
     * Creates a new HTTP client wrapper.
     *
     * @param config the client configuration
     */
    public HttpClientWrapper(final XposedOrNotConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(config.getTimeout())
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Returns the shared ObjectMapper instance.
     *
     * @return the object mapper
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Performs a GET request and deserializes the response.
     *
     * @param url           the full URL to request
     * @param responseType  the class to deserialize the response into
     * @param usePlusApi    whether this request targets the Plus API (skips rate limiting)
     * @param <T>           the response type
     * @return the deserialized response
     * @throws XposedOrNotException if the request fails
     */
    public <T> T get(final String url, final Class<T> responseType, final boolean usePlusApi)
            throws XposedOrNotException {
        final String body = getRaw(url, usePlusApi);
        try {
            return objectMapper.readValue(body, responseType);
        } catch (IOException e) {
            throw new ApiException("Failed to parse API response: " + e.getMessage(), 0);
        }
    }

    /**
     * Performs a GET request and returns the raw response body.
     *
     * @param url        the full URL to request
     * @param usePlusApi whether this request targets the Plus API (skips rate limiting)
     * @return the raw response body string
     * @throws XposedOrNotException if the request fails
     */
    public String getRaw(final String url, final boolean usePlusApi) throws XposedOrNotException {
        // Apply client-side rate limiting for free API only
        if (!config.hasApiKey() && !usePlusApi) {
            applyRateLimit();
        }

        final HttpRequest request = baseRequestBuilder(url).GET().build();
        return executeWithRetry(request);
    }

    public <T> T post(final String url, final Class<T> responseType, final boolean usePlusApi)
            throws XposedOrNotException {
        final String body = postRaw(url, usePlusApi);
        try {
            return objectMapper.readValue(body, responseType);
        } catch (IOException e) {
            throw new ApiException("Failed to parse API response: " + e.getMessage(), 0);
        }
    }

    public String postRaw(final String url, final boolean usePlusApi) throws XposedOrNotException {
        if (!config.hasApiKey() && !usePlusApi) {
            applyRateLimit();
        }

        final HttpRequest request = baseRequestBuilder(url)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return executeWithRetry(request);
    }

    private HttpRequest.Builder baseRequestBuilder(final String url) {
        final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(config.getTimeout())
                .header("Accept", "application/json")
                .header("User-Agent", "xposedornot-java/1.1.0");

        if (config.hasApiKey()) {
            requestBuilder.header("x-api-key", config.getApiKey());
        }

        for (final Map.Entry<String, String> entry : config.getCustomHeaders().entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }

        return requestBuilder;
    }

    private String executeWithRetry(final HttpRequest request) throws XposedOrNotException {
        int attempt = 0;
        final int maxRetries = config.getMaxRetries();

        while (true) {
            try {
                final HttpResponse<String> response = httpClient.send(
                        request, HttpResponse.BodyHandlers.ofString());

                final int statusCode = response.statusCode();

                if (statusCode >= 200 && statusCode < 300) {
                    return response.body();
                }

                if (statusCode == 429) {
                    if (attempt < maxRetries) {
                        final long delayMs = (long) Math.pow(2, attempt) * 1000L;
                        attempt++;
                        try {
                            Thread.sleep(delayMs);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new NetworkException("Request interrupted during retry backoff", ie);
                        }
                        continue;
                    }
                    throw new RateLimitException(
                            "Rate limit exceeded after " + maxRetries + " retries");
                }

                handleErrorStatus(statusCode, response.body());

            } catch (IOException e) {
                throw new NetworkException("Network error: " + e.getMessage(), e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NetworkException("Request interrupted: " + e.getMessage(), e);
            }
        }
    }

    private void handleErrorStatus(final int statusCode, final String body)
            throws XposedOrNotException {
        switch (statusCode) {
            case 401:
            case 403:
                throw new AuthenticationException(
                        "Authentication failed (HTTP " + statusCode + ")", statusCode);
            case 404:
                throw new NotFoundException("Resource not found");
            case 429:
                throw new RateLimitException("Rate limit exceeded");
            default:
                throw new ApiException(
                        "API request failed with status " + statusCode, statusCode, body);
        }
    }

    private void applyRateLimit() throws NetworkException {
        if (config.getApiKey() != null && !config.getApiKey().isEmpty()) {
            return;
        }

        synchronized (rateLimitLock) {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRequestTimeMs;
            long delayMs = RATE_LIMIT_INTERVAL_MS;

            if (elapsed < delayMs) {
                try {
                    Thread.sleep(delayMs - elapsed);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new NetworkException("Rate limit wait interrupted", e);
                }
            }

            lastRequestTimeMs = System.currentTimeMillis();
        }
    }

    @Override
    public void close() {
        // HttpClient does not require explicit cleanup in Java 11,
        // but we implement AutoCloseable for future compatibility.
    }
}
