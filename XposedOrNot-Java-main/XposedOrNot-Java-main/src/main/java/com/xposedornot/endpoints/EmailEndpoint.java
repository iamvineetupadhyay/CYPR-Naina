package com.xposedornot.endpoints;

import com.xposedornot.HttpClientWrapper;
import com.xposedornot.XposedOrNotConfig;
import com.xposedornot.exceptions.NotFoundException;
import com.xposedornot.exceptions.XposedOrNotException;
import com.xposedornot.models.BreachAnalyticsResponse;
import com.xposedornot.models.EmailBreachDetailedResponse;
import com.xposedornot.models.EmailBreachResponse;
import com.xposedornot.utils.EmailValidator;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Endpoint for email-related breach operations.
 *
 * <p>Provides methods for checking if an email has been involved in data breaches
 * using both the free and Plus (commercial) APIs.</p>
 */
public class EmailEndpoint {

    private final HttpClientWrapper httpClient;
    private final XposedOrNotConfig config;

    /**
     * Creates a new email endpoint.
     *
     * @param httpClient the HTTP client wrapper
     * @param config     the client configuration
     */
    public EmailEndpoint(final HttpClientWrapper httpClient, final XposedOrNotConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    /**
     * Checks if an email address has been involved in any known data breaches
     * using the free API.
     *
     * @param email the email address to check
     * @return the breach response containing breach names
     * @throws XposedOrNotException if the request fails or the email is invalid
     */
    public EmailBreachResponse check(final String email) throws XposedOrNotException {
        return check(email, false);
    }

    public EmailBreachResponse check(final String email, final boolean includeDetails)
            throws XposedOrNotException {
        EmailValidator.validate(email);
        final String encodedEmail = URLEncoder.encode(email.trim(), StandardCharsets.UTF_8);
        String url = config.getBaseUrl() + "/v1/check-email/" + encodedEmail;
        if (includeDetails) {
            url += "?include_details=true";
        }
        try {
            return httpClient.get(url, EmailBreachResponse.class, false);
        } catch (NotFoundException e) {
            return new EmailBreachResponse();
        }
    }

    /**
     * Checks if an email address has been involved in any known data breaches
     * using the Plus API with detailed breach information.
     *
     * <p>Requires an API key to be configured.</p>
     *
     * @param email the email address to check
     * @return the detailed breach response
     * @throws XposedOrNotException if the request fails, the email is invalid,
     *                              or no API key is configured
     */
    public EmailBreachDetailedResponse checkDetailed(final String email) throws XposedOrNotException {
        EmailValidator.validate(email);
        final String encodedEmail = URLEncoder.encode(email.trim(), StandardCharsets.UTF_8);
        final String url = config.getPlusBaseUrl() + "/v3/check-email/" + encodedEmail + "?detailed=true";
        return httpClient.get(url, EmailBreachDetailedResponse.class, true);
    }

    /**
     * Retrieves comprehensive breach analytics for an email address.
     *
     * @param email the email address to analyze
     * @return the analytics response containing breach details, metrics, and summaries
     * @throws XposedOrNotException if the request fails or the email is invalid
     */
    public BreachAnalyticsResponse getAnalytics(final String email) throws XposedOrNotException {
        return getAnalytics(email, null);
    }

    public BreachAnalyticsResponse getAnalytics(final String email, final String token)
            throws XposedOrNotException {
        EmailValidator.validate(email);
        final String encodedEmail = URLEncoder.encode(email.trim(), StandardCharsets.UTF_8);
        String url = config.getBaseUrl() + "/v1/breach-analytics?email=" + encodedEmail;
        if (token != null && !token.trim().isEmpty()) {
            url += "&token=" + URLEncoder.encode(token.trim(), StandardCharsets.UTF_8);
        }
        return httpClient.get(url, BreachAnalyticsResponse.class, false);
    }
}
