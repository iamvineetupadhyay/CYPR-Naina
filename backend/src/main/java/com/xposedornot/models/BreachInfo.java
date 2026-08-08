package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a breach entry from the breaches listing endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BreachInfo {

    @JsonProperty("breachID")
    private String breachId;

    @JsonProperty("breachedDate")
    private String breachedDate;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("industry")
    private String industry;

    @JsonProperty("exposedData")
    private Object exposedData;

    @JsonProperty("exposedRecords")
    private long exposedRecords;

    @JsonProperty("verified")
    private boolean verified;

    /** Default constructor for Jackson. */
    public BreachInfo() {
    }

    public String getBreachId() {
        return breachId;
    }

    public void setBreachId(final String breachId) {
        this.breachId = breachId;
    }

    public String getBreachedDate() {
        return breachedDate;
    }

    public void setBreachedDate(final String breachedDate) {
        this.breachedDate = breachedDate;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(final String industry) {
        this.industry = industry;
    }

    public Object getExposedData() {
        return exposedData;
    }

    public void setExposedData(final Object exposedData) {
        this.exposedData = exposedData;
    }

    public long getExposedRecords() {
        return exposedRecords;
    }

    public void setExposedRecords(final long exposedRecords) {
        this.exposedRecords = exposedRecords;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(final boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "BreachInfo{breachId='" + breachId + "', domain='" + domain
                + "', exposedRecords=" + exposedRecords + "}";
    }
}
