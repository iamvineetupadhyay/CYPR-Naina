package com.xposedornot.endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xposedornot.HttpClientWrapper;
import com.xposedornot.XposedOrNotConfig;
import com.xposedornot.exceptions.ApiException;
import com.xposedornot.exceptions.AuthenticationException;
import com.xposedornot.exceptions.XposedOrNotException;
import com.xposedornot.models.BreachInfo;
import com.xposedornot.models.DomainBreachesResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Endpoint for listing known data breaches.
 */
public class BreachesEndpoint {

    private final HttpClientWrapper httpClient;
    private final XposedOrNotConfig config;

    /**
     * Creates a new breaches endpoint.
     *
     * @param httpClient the HTTP client wrapper
     * @param config     the client configuration
     */
    public BreachesEndpoint(final HttpClientWrapper httpClient, final XposedOrNotConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    /**
     * Lists all known data breaches.
     *
     * @return a list of breach information entries
     * @throws XposedOrNotException if the request fails
     */
    public List<BreachInfo> list() throws XposedOrNotException {
        final String url = config.getBaseUrl() + "/v1/breaches";
        return parseBreachList(url);
    }

    /**
     * Lists breaches filtered by domain.
     *
     * @param domain the domain to filter by (e.g., "example.com")
     * @return a list of breach information entries for the given domain
     * @throws XposedOrNotException if the request fails
     */
    public List<BreachInfo> listByDomain(final String domain) throws XposedOrNotException {
        if (domain == null || domain.trim().isEmpty()) {
            return list();
        }
        final String encodedDomain = URLEncoder.encode(domain.trim(), StandardCharsets.UTF_8);
        final String url = config.getBaseUrl() + "/v1/breaches?domain=" + encodedDomain;
        return parseBreachList(url);
    }

    public List<BreachInfo> listByBreachId(final String breachId) throws XposedOrNotException {
        if (breachId == null || breachId.trim().isEmpty()) {
            return list();
        }
        final String encodedBreachId = URLEncoder.encode(breachId.trim(), StandardCharsets.UTF_8);
        final String url = config.getBaseUrl() + "/v1/breaches?breach_id=" + encodedBreachId;
        return parseBreachList(url);
    }

    public DomainBreachesResponse domainBreaches() throws XposedOrNotException {
        if (!config.hasApiKey()) {
            throw new AuthenticationException(
                    "An API key is required for domain breach monitoring. "
                            + "Verify your domains at https://xposedornot.com/dashboard", 401);
        }
        final String url = config.getBaseUrl() + "/v1/domain-breaches";
        return httpClient.post(url, DomainBreachesResponse.class, false);
    }

    private List<BreachInfo> parseBreachList(final String url) throws XposedOrNotException {
        final String body = httpClient.getRaw(url, false);
        try {
            final com.fasterxml.jackson.databind.JsonNode root = httpClient.getObjectMapper().readTree(body);
            final com.fasterxml.jackson.databind.JsonNode breachesNode = root.get("exposedBreaches");
            if (breachesNode == null || !breachesNode.isArray()) {
                return List.of();
            }
            return httpClient.getObjectMapper().readValue(
                    breachesNode.toString(), new TypeReference<List<BreachInfo>>() {});
        } catch (IOException e) {
            throw new ApiException("Failed to parse breaches response: " + e.getMessage(), 0, body);
        }
    }
}
