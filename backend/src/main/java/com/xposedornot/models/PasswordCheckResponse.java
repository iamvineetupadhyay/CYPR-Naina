package com.xposedornot.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response from the anonymous password check endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordCheckResponse {

    @JsonProperty("SearchPassAnon")
    private SearchPassAnon searchPassAnon;

    /** Default constructor for Jackson. */
    public PasswordCheckResponse() {
    }

    /**
     * Creates a not-found response (password is clean).
     *
     * @param hashPrefix the hash prefix that was checked
     * @return a response with zero exposure count
     */
    public static PasswordCheckResponse notFound(final String hashPrefix) {
        final PasswordCheckResponse resp = new PasswordCheckResponse();
        final SearchPassAnon anon = new SearchPassAnon();
        anon.setAnon(hashPrefix);
        anon.setCharBreakdown("");
        anon.setCount("0");
        resp.setSearchPassAnon(anon);
        return resp;
    }

    public SearchPassAnon getSearchPassAnon() {
        return searchPassAnon;
    }

    public void setSearchPassAnon(final SearchPassAnon searchPassAnon) {
        this.searchPassAnon = searchPassAnon;
    }

    /**
     * Returns whether the password was found in any breach.
     *
     * @return true if the password exposure count is greater than zero
     */
    public boolean isExposed() {
        if (searchPassAnon == null || searchPassAnon.getCount() == null) {
            return false;
        }
        try {
            return Long.parseLong(searchPassAnon.getCount()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns the number of times this password has been exposed.
     *
     * @return the exposure count, or 0 if unknown
     */
    public long getExposureCount() {
        if (searchPassAnon == null || searchPassAnon.getCount() == null) {
            return 0;
        }
        try {
            return Long.parseLong(searchPassAnon.getCount());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "PasswordCheckResponse{searchPassAnon=" + searchPassAnon + "}";
    }

    /**
     * Inner data from the password check response.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchPassAnon {

        @JsonProperty("anon")
        private String anon;

        @JsonProperty("char")
        private String charBreakdown;

        @JsonProperty("count")
        private String count;

        /** Default constructor for Jackson. */
        public SearchPassAnon() {
        }

        public String getAnon() {
            return anon;
        }

        public void setAnon(final String anon) {
            this.anon = anon;
        }

        public String getCharBreakdown() {
            return charBreakdown;
        }

        public void setCharBreakdown(final String charBreakdown) {
            this.charBreakdown = charBreakdown;
        }

        public String getCount() {
            return count;
        }

        public void setCount(final String count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "SearchPassAnon{anon='" + anon + "', count='" + count + "'}";
        }
    }
}
