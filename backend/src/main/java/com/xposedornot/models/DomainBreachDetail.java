package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainBreachDetail {

    @JsonProperty("email")
    private String email;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("breach")
    private String breach;

    public DomainBreachDetail() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }

    public String getBreach() {
        return breach;
    }

    public void setBreach(final String breach) {
        this.breach = breach;
    }

    @Override
    public String toString() {
        return "DomainBreachDetail{email='" + email + "', domain='" + domain
                + "', breach='" + breach + "'}";
    }
}
