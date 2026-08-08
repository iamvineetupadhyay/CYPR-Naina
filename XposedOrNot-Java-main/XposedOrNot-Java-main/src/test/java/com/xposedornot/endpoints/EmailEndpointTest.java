package com.xposedornot.endpoints;

import com.xposedornot.XposedOrNot;
import com.xposedornot.exceptions.NotFoundException;
import com.xposedornot.exceptions.ValidationException;
import com.xposedornot.models.BreachAnalyticsResponse;
import com.xposedornot.models.EmailBreachDetailedResponse;
import com.xposedornot.models.EmailBreachResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the EmailEndpoint using MockWebServer.
 */
class EmailEndpointTest {

    private MockWebServer mockServer;
    private XposedOrNot client;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        final String baseUrl = mockServer.url("/").toString();
        // Remove trailing slash
        final String base = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        client = XposedOrNot.builder()
                .baseUrl(base)
                .plusBaseUrl(base)
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
    void checkEmailReturnsBreaches() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setBody("{\"breaches\":[[\"Adobe\",\"LinkedIn\"]]}")
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final EmailBreachResponse response = client.email().check("test@example.com");

        assertNotNull(response);
        assertEquals(2, response.getBreachNames().size());
        assertTrue(response.getBreachNames().contains("Adobe"));
        assertTrue(response.getBreachNames().contains("LinkedIn"));

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/check-email/test%40example.com", request.getPath());
    }

    @Test
    void checkEmailParsesFlatBreachList() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setBody("{\"breaches\":[\"Adobe\",\"LinkedIn\"],"
                        + "\"email\":\"test@example.com\",\"status\":\"success\"}")
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final EmailBreachResponse response = client.email().check("test@example.com");

        assertNotNull(response);
        assertEquals(List.of("Adobe", "LinkedIn"), response.getBreachNames());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("success", response.getStatus());
    }

    @Test
    void checkEmailFlattensMultipleNestedLists() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setBody("{\"breaches\":[[\"Adobe\"],[\"LinkedIn\"]]}")
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final EmailBreachResponse response = client.email().check("test@example.com");

        assertEquals(List.of("Adobe", "LinkedIn"), response.getBreachNames());
    }

    @Test
    void checkEmailWithIncludeDetailsSendsQueryParam() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setBody("{\"breaches\":[[\"Adobe\"]]}")
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final EmailBreachResponse response = client.email().check("test@example.com", true);

        assertEquals(List.of("Adobe"), response.getBreachNames());

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/check-email/test%40example.com?include_details=true", request.getPath());
    }

    @Test
    void checkEmailValidatesInput() {
        assertThrows(ValidationException.class, () -> client.email().check(null));
        assertThrows(ValidationException.class, () -> client.email().check(""));
        assertThrows(ValidationException.class, () -> client.email().check("not-an-email"));
    }

    @Test
    void checkDetailedReturnsDetailedBreaches() throws Exception {
        final String json = "{\"status\":\"success\",\"email\":\"test@example.com\","
                + "\"breaches\":[{\"breach_id\":\"Adobe\",\"breached_date\":\"2013-10-04\","
                + "\"domain\":\"adobe.com\",\"xposed_records\":153000000}]}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final EmailBreachDetailedResponse response = client.email().checkDetailed("test@example.com");

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(1, response.getBreaches().size());
        assertEquals("Adobe", response.getBreaches().get(0).getBreachId());
        assertEquals(153000000, response.getBreaches().get(0).getXposedRecords());

        final RecordedRequest request = mockServer.takeRequest();
        assertTrue(request.getPath().startsWith("/v3/check-email/test%40example.com"));
        assertTrue(request.getPath().contains("detailed=true"));
    }

    @Test
    void checkEmailHandles404() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setBody("{\"error\":\"not found\"}")
                .setResponseCode(404));

        EmailBreachResponse result = client.email().check("notfound@example.com");
        assertTrue(result.getBreachNames().isEmpty());
    }

    @Test
    void getAnalyticsReturnsBreachAnalytics() throws Exception {
        final String json = "{\"ExposedBreaches\":{\"breaches_details\":[{\"breach\":\"Adobe\","
                + "\"domain\":\"adobe.com\"}]},\"BreachesSummary\":{},\"BreachMetrics\":{},"
                + "\"PastesSummary\":{},\"ExposedPastes\":[]}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final BreachAnalyticsResponse response = client.email().getAnalytics("test@example.com");

        assertNotNull(response);
        assertNotNull(response.getExposedBreaches());
        assertEquals(1, response.getExposedBreaches().getBreachesDetails().size());
        assertEquals("Adobe", response.getExposedBreaches().getBreachesDetails().get(0).getBreach());

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/breach-analytics?email=test%40example.com", request.getPath());
    }

    @Test
    void getAnalyticsWithTokenSendsQueryParam() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setBody("{\"ExposedBreaches\":{\"breaches_details\":[]}}")
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        client.email().getAnalytics("test@example.com", "secret-token");

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/breach-analytics?email=test%40example.com&token=secret-token",
                request.getPath());
    }

    @Test
    void getAnalyticsParsesBreachesSummary() throws Exception {
        final String json = "{\"ExposedBreaches\":{\"breaches_details\":[{\"breach\":\"Adobe\"},"
                + "{\"breach\":\"LinkedIn\"}]},"
                + "\"BreachesSummary\":{\"site\":\"Adobe;LinkedIn\",\"exposures\":5,"
                + "\"first_breach\":\"2013-10-04\"},"
                + "\"PastesSummary\":{\"cnt\":2}}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final BreachAnalyticsResponse response = client.email().getAnalytics("test@example.com");

        assertEquals(List.of("Adobe", "LinkedIn"), response.getBreachNames());
        assertEquals(2, response.getBreachesCount());
        assertEquals(5, response.getExposuresCount());
        assertEquals("2013-10-04", response.getFirstBreach());
        assertEquals(2, response.getPastesCount());
    }
}
