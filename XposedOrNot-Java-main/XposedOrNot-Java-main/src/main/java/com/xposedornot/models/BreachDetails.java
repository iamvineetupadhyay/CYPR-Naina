package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a breach detail entry from the breach analytics response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BreachDetails {

    @JsonProperty("breach")
    private String breach;

    @JsonProperty("details")
    private String details;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("industry")
    private String industry;

    @JsonProperty("logo")
    private String logo;

    @JsonProperty("password_risk")
    private String passwordRisk;

    @JsonProperty("references")
    private String references;

    @JsonProperty("searchable")
    private String searchable;

    @JsonProperty("verified")
    private String verified;

    @JsonProperty("xposed_data")
    private String xposedData;

    @JsonProperty("xposed_date")
    private String xposedDate;

    @JsonProperty("xposed_records")
    private long xposedRecords;

    /** Default constructor for Jackson. */
    public BreachDetails() {
    }

    public String getBreach() {
        return breach;
    }

    public void setBreach(final String breach) {
        this.breach = breach;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
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

    public String getReferences() {
        return references;
    }

    public void setReferences(final String references) {
        this.references = references;
    }

    public String getSearchable() {
        return searchable;
    }

    public void setSearchable(final String searchable) {
        this.searchable = searchable;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(final String verified) {
        this.verified = verified;
    }

    public String getXposedData() {
        return xposedData;
    }

    public void setXposedData(final String xposedData) {
        this.xposedData = xposedData;
    }

    public String getXposedDate() {
        return xposedDate;
    }

    public void setXposedDate(final String xposedDate) {
        this.xposedDate = xposedDate;
    }

    public long getXposedRecords() {
        return xposedRecords;
    }

    public void setXposedRecords(final long xposedRecords) {
        this.xposedRecords = xposedRecords;
    }

    @Override
    public String toString() {
        return "BreachDetails{breach='" + breach + "', domain='" + domain + "'}";
    }
}
