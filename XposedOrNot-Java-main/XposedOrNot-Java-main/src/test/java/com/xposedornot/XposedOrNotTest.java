package com.xposedornot;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the main XposedOrNot client builder and configuration.
 */
class XposedOrNotTest {

    @Test
    void builderCreatesClientWithDefaults() {
        try (XposedOrNot client = XposedOrNot.builder().build()) {
            assertNotNull(client);
            assertNotNull(client.email());
            assertNotNull(client.breaches());
            assertNotNull(client.password());
            assertEquals(XposedOrNotConfig.DEFAULT_BASE_URL, client.getConfig().getBaseUrl());
            assertEquals(XposedOrNotConfig.PLUS_BASE_URL, client.getConfig().getPlusBaseUrl());
            assertEquals(XposedOrNotConfig.PASSWORD_BASE_URL, client.getConfig().getPasswordBaseUrl());
            assertEquals(Duration.ofSeconds(30), client.getConfig().getTimeout());
            assertEquals(3, client.getConfig().getMaxRetries());
            assertFalse(client.getConfig().hasApiKey());
        }
    }

    @Test
    void builderAppliesCustomConfiguration() {
        try (XposedOrNot client = XposedOrNot.builder()
                .baseUrl("http://localhost:8080")
                .plusBaseUrl("http://localhost:8081")
                .passwordBaseUrl("http://localhost:8082")
                .timeout(Duration.ofSeconds(10))
                .maxRetries(5)
                .apiKey("test-key")
                .header("X-Custom", "value")
                .allowInsecure(true)
                .build()) {

            assertEquals("http://localhost:8080", client.getConfig().getBaseUrl());
            assertEquals("http://localhost:8081", client.getConfig().getPlusBaseUrl());
            assertEquals("http://localhost:8082", client.getConfig().getPasswordBaseUrl());
            assertEquals(Duration.ofSeconds(10), client.getConfig().getTimeout());
            assertEquals(5, client.getConfig().getMaxRetries());
            assertTrue(client.getConfig().hasApiKey());
            assertEquals("test-key", client.getConfig().getApiKey());
            assertEquals("value", client.getConfig().getCustomHeaders().get("X-Custom"));
        }
    }

    @Test
    void builderStripsTrailingSlash() {
        try (XposedOrNot client = XposedOrNot.builder()
                .baseUrl("http://localhost:8080/")
                .allowInsecure(true)
                .build()) {
            assertEquals("http://localhost:8080", client.getConfig().getBaseUrl());
        }
    }

    @Test
    void builderRejectsNegativeRetries() {
        assertThrows(IllegalArgumentException.class, () ->
                XposedOrNot.builder().maxRetries(-1));
    }

    @Test
    void hasApiKeyReturnsFalseForNull() {
        try (XposedOrNot client = XposedOrNot.builder().build()) {
            assertFalse(client.getConfig().hasApiKey());
        }
    }

    @Test
    void hasApiKeyReturnsFalseForEmpty() {
        try (XposedOrNot client = XposedOrNot.builder().apiKey("  ").build()) {
            assertFalse(client.getConfig().hasApiKey());
        }
    }

    @Test
    void hasApiKeyReturnsTrueForValidKey() {
        try (XposedOrNot client = XposedOrNot.builder().apiKey("valid-key").build()) {
            assertTrue(client.getConfig().hasApiKey());
        }
    }
}
