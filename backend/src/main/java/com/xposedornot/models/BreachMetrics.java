package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Represents breach metrics from the breach analytics response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BreachMetrics {

    @JsonProperty("industry")
    private Object industry;

    @JsonProperty("passwords_strength")
    private Object passwordsStrength;

    @JsonProperty("xposed_data")
    private Object xposedData;

    @JsonProperty("yearwise_details")
    private Object yearwiseDetails;

    /** Default constructor for Jackson. */
    public BreachMetrics() {
    }

    public Object getIndustry() {
        return industry;
    }

    public void setIndustry(final Object industry) {
        this.industry = industry;
    }

    public Object getPasswordsStrength() {
        return passwordsStrength;
    }

    public void setPasswordsStrength(final Object passwordsStrength) {
        this.passwordsStrength = passwordsStrength;
    }

    public Object getXposedData() {
        return xposedData;
    }

    public void setXposedData(final Object xposedData) {
        this.xposedData = xposedData;
    }

    public Object getYearwiseDetails() {
        return yearwiseDetails;
    }

    public void setYearwiseDetails(final Object yearwiseDetails) {
        this.yearwiseDetails = yearwiseDetails;
    }

    @Override
    public String toString() {
        return "BreachMetrics{industry=" + industry + "}";
    }
}
