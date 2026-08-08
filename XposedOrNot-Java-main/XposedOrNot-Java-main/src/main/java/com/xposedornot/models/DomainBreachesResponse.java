package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DomainBreachesResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("metrics")
    private Metrics metrics;

    public DomainBreachesResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(final Metrics metrics) {
        this.metrics = metrics;
    }

    public List<DomainBreachDetail> getBreachesDetails() {
        if (metrics == null || metrics.breachesDetails == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(metrics.breachesDetails);
    }

    public Map<String, Object> getYearlyMetrics() {
        return metrics == null ? Collections.emptyMap() : safeMap(metrics.yearlyMetrics);
    }

    public Map<String, Object> getDomainSummary() {
        return metrics == null ? Collections.emptyMap() : safeMap(metrics.domainSummary);
    }

    public Map<String, Object> getBreachSummary() {
        return metrics == null ? Collections.emptyMap() : safeMap(metrics.breachSummary);
    }

    public Map<String, Object> getTop10Breaches() {
        return metrics == null ? Collections.emptyMap() : safeMap(metrics.top10Breaches);
    }

    public Map<String, Object> getDetailedBreachInfo() {
        return metrics == null ? Collections.emptyMap() : safeMap(metrics.detailedBreachInfo);
    }

    private static Map<String, Object> safeMap(final Map<String, Object> map) {
        return map == null ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }

    @Override
    public String toString() {
        return "DomainBreachesResponse{status='" + status
                + "', breachesDetails=" + getBreachesDetails().size() + "}";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metrics {

        @JsonProperty("Breaches_Details")
        private List<DomainBreachDetail> breachesDetails;

        @JsonProperty("Yearly_Metrics")
        private Map<String, Object> yearlyMetrics;

        @JsonProperty("Domain_Summary")
        private Map<String, Object> domainSummary;

        @JsonProperty("Breach_Summary")
        private Map<String, Object> breachSummary;

        @JsonProperty("Top10_Breaches")
        private Map<String, Object> top10Breaches;

        @JsonProperty("Detailed_Breach_Info")
        private Map<String, Object> detailedBreachInfo;

        public Metrics() {
        }

        public List<DomainBreachDetail> getBreachesDetails() {
            return breachesDetails;
        }

        public void setBreachesDetails(final List<DomainBreachDetail> breachesDetails) {
            this.breachesDetails = breachesDetails;
        }

        public Map<String, Object> getYearlyMetrics() {
            return yearlyMetrics;
        }

        public void setYearlyMetrics(final Map<String, Object> yearlyMetrics) {
            this.yearlyMetrics = yearlyMetrics;
        }

        public Map<String, Object> getDomainSummary() {
            return domainSummary;
        }

        public void setDomainSummary(final Map<String, Object> domainSummary) {
            this.domainSummary = domainSummary;
        }

        public Map<String, Object> getBreachSummary() {
            return breachSummary;
        }

        public void setBreachSummary(final Map<String, Object> breachSummary) {
            this.breachSummary = breachSummary;
        }

        public Map<String, Object> getTop10Breaches() {
            return top10Breaches;
        }

        public void setTop10Breaches(final Map<String, Object> top10Breaches) {
            this.top10Breaches = top10Breaches;
        }

        public Map<String, Object> getDetailedBreachInfo() {
            return detailedBreachInfo;
        }

        public void setDetailedBreachInfo(final Map<String, Object> detailedBreachInfo) {
            this.detailedBreachInfo = detailedBreachInfo;
        }
    }
}
