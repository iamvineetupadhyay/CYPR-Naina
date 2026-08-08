package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Response from the breach analytics endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BreachAnalyticsResponse {

    @JsonProperty("ExposedBreaches")
    private ExposedBreaches exposedBreaches;

    @JsonProperty("BreachesSummary")
    private Map<String, Object> breachesSummary;

    @JsonProperty("BreachMetrics")
    private BreachMetrics breachMetrics;

    @JsonProperty("PastesSummary")
    private Map<String, Object> pastesSummary;

    @JsonProperty("ExposedPastes")
    private List<Map<String, Object>> exposedPastes;

    /** Default constructor for Jackson. */
    public BreachAnalyticsResponse() {
    }

    public ExposedBreaches getExposedBreaches() {
        return exposedBreaches;
    }

    public void setExposedBreaches(final ExposedBreaches exposedBreaches) {
        this.exposedBreaches = exposedBreaches;
    }

    public Map<String, Object> getBreachesSummary() {
        return breachesSummary;
    }

    public void setBreachesSummary(final Map<String, Object> breachesSummary) {
        this.breachesSummary = breachesSummary;
    }

    public BreachMetrics getBreachMetrics() {
        return breachMetrics;
    }

    public void setBreachMetrics(final BreachMetrics breachMetrics) {
        this.breachMetrics = breachMetrics;
    }

    public Map<String, Object> getPastesSummary() {
        return pastesSummary;
    }

    public void setPastesSummary(final Map<String, Object> pastesSummary) {
        this.pastesSummary = pastesSummary;
    }

    public List<Map<String, Object>> getExposedPastes() {
        return exposedPastes;
    }

    public void setExposedPastes(final List<Map<String, Object>> exposedPastes) {
        this.exposedPastes = exposedPastes;
    }

    public List<String> getBreachNames() {
        final Object site = breachesSummary == null ? null : breachesSummary.get("site");
        if (!(site instanceof String) || ((String) site).isEmpty()) {
            return Collections.emptyList();
        }
        final List<String> names = new ArrayList<>();
        for (final String name : ((String) site).split(";")) {
            if (!name.isEmpty()) {
                names.add(name);
            }
        }
        return Collections.unmodifiableList(names);
    }

    public int getBreachesCount() {
        final List<String> names = getBreachNames();
        if (!names.isEmpty()) {
            return names.size();
        }
        final Object site = breachesSummary == null ? null : breachesSummary.get("site");
        if (site instanceof Number) {
            return ((Number) site).intValue();
        }
        return 0;
    }

    public int getExposuresCount() {
        final Object exposures = breachesSummary == null ? null : breachesSummary.get("exposures");
        if (exposures instanceof Number) {
            return ((Number) exposures).intValue();
        }
        if (exposedBreaches != null && exposedBreaches.getBreachesDetails() != null) {
            return exposedBreaches.getBreachesDetails().size();
        }
        return 0;
    }

    public String getFirstBreach() {
        final Object firstBreach = breachesSummary == null ? null : breachesSummary.get("first_breach");
        return firstBreach instanceof String ? (String) firstBreach : "";
    }

    public int getPastesCount() {
        final Object cnt = pastesSummary == null ? null : pastesSummary.get("cnt");
        return cnt instanceof Number ? ((Number) cnt).intValue() : 0;
    }

    @Override
    public String toString() {
        return "BreachAnalyticsResponse{exposedBreaches=" + exposedBreaches + "}";
    }

    /**
     * Wrapper for the ExposedBreaches field containing breach details.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExposedBreaches {

        @JsonProperty("breaches_details")
        private List<BreachDetails> breachesDetails;

        /** Default constructor for Jackson. */
        public ExposedBreaches() {
        }

        public List<BreachDetails> getBreachesDetails() {
            return breachesDetails;
        }

        public void setBreachesDetails(final List<BreachDetails> breachesDetails) {
            this.breachesDetails = breachesDetails;
        }

        @Override
        public String toString() {
            return "ExposedBreaches{count=" + (breachesDetails != null ? breachesDetails.size() : 0) + "}";
        }
    }
}
