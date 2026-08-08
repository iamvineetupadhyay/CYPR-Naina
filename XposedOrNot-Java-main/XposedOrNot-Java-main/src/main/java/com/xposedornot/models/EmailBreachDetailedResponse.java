package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response from the Plus API detailed email breach check endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailBreachDetailedResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("email")
    private String email;

    @JsonProperty("breaches")
    private List<Breach> breaches;

    /** Default constructor for Jackson. */
    public EmailBreachDetailedResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public List<Breach> getBreaches() {
        return breaches;
    }

    public void setBreaches(final List<Breach> breaches) {
        this.breaches = breaches;
    }

    @Override
    public String toString() {
        return "EmailBreachDetailedResponse{status='" + status + "', email='" + email
                + "', breachCount=" + (breaches != null ? breaches.size() : 0) + "}";
    }
}
