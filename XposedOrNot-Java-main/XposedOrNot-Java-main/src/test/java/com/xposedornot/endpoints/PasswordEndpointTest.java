package com.xposedornot.endpoints;

import com.xposedornot.XposedOrNot;
import com.xposedornot.exceptions.ValidationException;
import com.xposedornot.models.PasswordCheckResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the PasswordEndpoint using MockWebServer.
 */
class PasswordEndpointTest {

    private MockWebServer mockServer;
    private XposedOrNot client;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        final String base = mockServer.url("/").toString().replaceAll("/$", "");
        client = XposedOrNot.builder()
                .passwordBaseUrl(base)
                .maxRetries(0)
                .allowInsecure(true)
                .build();
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
        mockServer.shutdown();
    }

    @Test
    void checkPasswordReturnsExposureInfo() throws Exception {
        final String json = "{\"SearchPassAnon\":{\"anon\":\"0a4f4a4e07\","
                + "\"char\":\"D:3;A:8;S:0;L:11\",\"count\":\"62703\"}}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final PasswordCheckResponse response = client.password().check("password123");

        assertNotNull(response);
        assertNotNull(response.getSearchPassAnon());
        assertEquals("62703", response.getSearchPassAnon().getCount());
        assertTrue(response.isExposed());
        assertEquals(62703, response.getExposureCount());

        final RecordedRequest request = mockServer.takeRequest();
        assertTrue(request.getPath().startsWith("/v1/pass/anon/"));
        // Hash prefix should be 10 characters
        final String path = request.getPath();
        final String prefix = path.substring(path.lastIndexOf('/') + 1);
        assertEquals(10, prefix.length());
    }

    @Test
    void checkPasswordValidatesInput() {
        assertThrows(ValidationException.class, () -> client.password().check(null));
        assertThrows(ValidationException.class, () -> client.password().check(""));
    }

    @Test
    void checkByHashPrefixValidatesLength() {
        assertThrows(ValidationException.class, () ->
                client.password().checkByHashPrefix("short"));
        assertThrows(ValidationException.class, () ->
                client.password().checkByHashPrefix(null));
    }

    @Test
    void checkByHashPrefixValidatesHexChars() {
        assertThrows(ValidationException.class, () ->
                client.password().checkByHashPrefix("zzzzzzzzzz"));
    }

    @Test
    void checkByHashPrefixSendsCorrectRequest() throws Exception {
        final String json = "{\"SearchPassAnon\":{\"anon\":\"0a4f4a4e07\","
                + "\"char\":\"D:3;A:8;S:0;L:11\",\"count\":\"0\"}}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final PasswordCheckResponse response = client.password().checkByHashPrefix("0a4f4a4e07");

        assertNotNull(response);
        assertFalse(response.isExposed());
        assertEquals(0, response.getExposureCount());

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/pass/anon/0a4f4a4e07", request.getPath());
    }
}
