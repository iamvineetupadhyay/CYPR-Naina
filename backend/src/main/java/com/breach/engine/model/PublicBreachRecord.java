package com.breach.engine.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "public_breaches")
public class PublicBreachRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String domain;

    private String breachDate;

    private Long pwnCount;

    @Column(length = 2000)
    private String description;

    private String dataClasses; // Comma separated, e.g. Passwords, Emails, IP addresses

    private Boolean isVerified;

    private Boolean isFabricated;

    private Boolean isSensitive;

    private String severity; // CRITICAL, HIGH, MEDIUM, LOW

    private String sourceUrl;

    private LocalDateTime scrapedAt;

    public PublicBreachRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBreachDate() {
        return breachDate;
    }

    public void setBreachDate(String breachDate) {
        this.breachDate = breachDate;
    }

    public Long getPwnCount() {
        return pwnCount;
    }

    public void setPwnCount(Long pwnCount) {
        this.pwnCount = pwnCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataClasses() {
        return dataClasses;
    }

    public void setDataClasses(String dataClasses) {
        this.dataClasses = dataClasses;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Boolean getIsFabricated() {
        return isFabricated;
    }

    public void setFabricated(Boolean fabricated) {
        isFabricated = fabricated;
    }

    public Boolean getIsSensitive() {
        return isSensitive;
    }

    public void setSensitive(Boolean sensitive) {
        isSensitive = sensitive;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public LocalDateTime getScrapedAt() {
        return scrapedAt;
    }

    public void setScrapedAt(LocalDateTime scrapedAt) {
        this.scrapedAt = scrapedAt;
    }
}
