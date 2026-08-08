package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a detailed breach entry from the Plus API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Breach {

    @JsonProperty("breach_id")
    private String breachId;

    @JsonProperty("breached_date")
    private String breachedDate;

    @JsonProperty("logo")
    private String logo;

    @JsonProperty("password_risk")
    private String passwordRisk;

    @JsonProperty("searchable")
    private String searchable;

    @JsonProperty("xposed_data")
    private String xposedData;

    @JsonProperty("xposed_records")
    private long xposedRecords;

    @JsonProperty("xposure_desc")
    private String xposureDesc;

    @JsonProperty("domain")
    private String domain;

    /** Default constructor for Jackson. */
    public Breach() {
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(final String logo) {
        this.logo = logo;
    }

    public String getPasswordRisk() {
        return passwordRisk;
    }

    public void setPasswordRisk(final String passwordRisk) {
        this.passwordRisk = passwordRisk;
    }

    public String getSearchable() {
        return searchable;
    }

    public void setSearchable(final String searchable) {
        this.searchable = searchable;
    }

    public String getXposedData() {
        return xposedData;
    }

    public void setXposedData(final String xposedData) {
        this.xposedData = xposedData;
    }

    public long getXposedRecords() {
        return xposedRecords;
    }

    public void setXposedRecords(final long xposedRecords) {
        this.xposedRecords = xposedRecords;
    }

    public String getXposureDesc() {
        return xposureDesc;
    }

    public void setXposureDesc(final String xposureDesc) {
        this.xposureDesc = xposureDesc;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "Breach{breachId='" + breachId + "', domain='" + domain + "', xposedRecords=" + xposedRecords + "}";
    }
}
