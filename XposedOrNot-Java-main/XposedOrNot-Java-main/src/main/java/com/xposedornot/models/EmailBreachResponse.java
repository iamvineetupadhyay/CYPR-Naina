package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Response from the free email breach check endpoint.
 * The API returns breaches as a nested array: {@code {"breaches": [["breach1","breach2"]]}}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailBreachResponse {

    private List<List<String>> breaches;
    private List<String> breachNames = new ArrayList<>();

    @JsonProperty("email")
    private String email;

    @JsonProperty("status")
    private String status;

    /** Default constructor for Jackson. */
    public EmailBreachResponse() {
    }

    @JsonProperty("breaches")
    private void unpackBreaches(final JsonNode node) {
        final List<String> flat = new ArrayList<>();
        final List<List<String>> nested = new ArrayList<>();
        if (node != null && node.isArray()) {
            for (final JsonNode item : node) {
                if (item.isArray()) {
                    final List<String> inner = new ArrayList<>();
                    for (final JsonNode name : item) {
                        inner.add(name.asText());
                    }
                    nested.add(inner);
                    flat.addAll(inner);
                } else {
                    flat.add(item.asText());
                }
            }
        }
        if (nested.isEmpty() && !flat.isEmpty()) {
            nested.add(new ArrayList<>(flat));
        }
        this.breaches = nested;
        this.breachNames = flat;
    }

    /**
     * Returns the raw nested breach list from the API.
     *
     * @return the nested list of breach names
     */
    @JsonIgnore
    public List<List<String>> getBreaches() {
        return breaches;
    }

    @JsonIgnore
    public void setBreaches(final List<List<String>> breaches) {
        this.breaches = breaches;
        final List<String> flat = new ArrayList<>();
        if (breaches != null) {
            for (final List<String> inner : breaches) {
                if (inner != null) {
                    flat.addAll(inner);
                }
            }
        }
        this.breachNames = flat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * Convenience method to get the flat list of breach names.
     *
     * @return a flat list of breach name strings
     */
    public List<String> getBreachNames() {
        return Collections.unmodifiableList(breachNames);
    }

    @Override
    public String toString() {
        return "EmailBreachResponse{breaches=" + breachNames
                + ", email='" + email + "', status='" + status + "'}";
    }
}
