package com.xposedornot.endpoints;

import com.xposedornot.XposedOrNot;
import com.xposedornot.exceptions.AuthenticationException;
import com.xposedornot.exceptions.XposedOrNotException;
import com.xposedornot.models.BreachInfo;
import com.xposedornot.models.DomainBreachesResponse;
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
 * Tests for the BreachesEndpoint using MockWebServer.
 */
class BreachesEndpointTest {

    private MockWebServer mockServer;
    private XposedOrNot client;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        final String base = mockServer.url("/").toString().replaceAll("/$", "");
        client = XposedOrNot.builder()
                .baseUrl(base)
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
    void listReturnsAllBreaches() throws Exception {
        final String json = "{\"exposedBreaches\":[{\"breachID\":\"Adobe\","
                + "\"breachedDate\":\"2013-10-04\",\"domain\":\"adobe.com\","
                + "\"industry\":\"Technology\",\"exposedData\":\"emails,passwords\","
                + "\"exposedRecords\":153000000,\"verified\":true}]}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final List<BreachInfo> breaches = client.breaches().list();

        assertNotNull(breaches);
        assertEquals(1, breaches.size());
        assertEquals("Adobe", breaches.get(0).getBreachId());
        assertEquals("adobe.com", breaches.get(0).getDomain());
        assertEquals(153000000, breaches.get(0).getExposedRecords());
        assertTrue(breaches.get(0).isVerified());

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/breaches", request.getPath());
    }

    @Test
    void listByDomainFiltersResults() throws Exception {
        final String json = "{\"exposedBreaches\":[{\"breachID\":\"Adobe\","
                + "\"domain\":\"adobe.com\",\"exposedRecords\":153000000}]}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final List<BreachInfo> breaches = client.breaches().listByDomain("adobe.com");

        assertNotNull(breaches);
        assertEquals(1, breaches.size());

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/breaches?domain=adobe.com", request.getPath());
    }

    @Test
    void listByDomainWithNullDelegatesToList() throws Exception {
        final String json = "{\"exposedBreaches\":[]}";
        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200));

        final List<BreachInfo> breaches = client.breaches().listByDomain(null);
        assertNotNull(breaches);
        assertTrue(breaches.isEmpty());

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/breaches", request.getPath());
    }

    @Test
    void listByBreachIdSendsQueryParam() throws Exception {
        final String json = "{\"exposedBreaches\":[{\"breachID\":\"Adobe\","
                + "\"domain\":\"adobe.com\",\"exposedRecords\":153000000}]}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final List<BreachInfo> breaches = client.breaches().listByBreachId("Adobe");

        assertNotNull(breaches);
        assertEquals(1, breaches.size());
        assertEquals("Adobe", breaches.get(0).getBreachId());

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/breaches?breach_id=Adobe", request.getPath());
    }

    @Test
    void listByBreachIdWithNullDelegatesToList() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setBody("{\"exposedBreaches\":[]}")
                .setResponseCode(200));

        final List<BreachInfo> breaches = client.breaches().listByBreachId(null);
        assertNotNull(breaches);
        assertTrue(breaches.isEmpty());

        final RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v1/breaches", request.getPath());
    }

    @Test
    void domainBreachesWithoutApiKeyThrows() {
        assertThrows(AuthenticationException.class, () -> client.breaches().domainBreaches());
    }

    @Test
    void domainBreachesReturnsMetrics() throws Exception {
        final String json = "{\"status\":\"success\",\"metrics\":{"
                + "\"Breaches_Details\":[{\"email\":\"user@example.com\","
                + "\"domain\":\"example.com\",\"breach\":\"Adobe\"}],"
                + "\"Yearly_Metrics\":{\"2013\":1},"
                + "\"Domain_Summary\":{\"example.com\":1},"
                + "\"Breach_Summary\":{\"Adobe\":1},"
                + "\"Top10_Breaches\":{\"Adobe\":1},"
                + "\"Detailed_Breach_Info\":{\"Adobe\":{\"breached_date\":\"2013-10-04\"}}}}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        final String base = mockServer.url("/").toString().replaceAll("/$", "");
        try (XposedOrNot keyedClient = XposedOrNot.builder()
                .baseUrl(base)
                .apiKey("test-api-key")
                .maxRetries(0)
                .allowInsecure(true)
                .build()) {

            final DomainBreachesResponse response = keyedClient.breaches().domainBreaches();

            assertNotNull(response);
            assertEquals("success", response.getStatus());
            assertEquals(1, response.getBreachesDetails().size());
            assertEquals("user@example.com", response.getBreachesDetails().get(0).getEmail());
            assertEquals("example.com", response.getBreachesDetails().get(0).getDomain());
            assertEquals("Adobe", response.getBreachesDetails().get(0).getBreach());
            assertEquals(1, response.getYearlyMetrics().get("2013"));
            assertEquals(1, response.getDomainSummary().get("example.com"));
            assertEquals(1, response.getBreachSummary().get("Adobe"));
            assertEquals(1, response.getTop10Breaches().get("Adobe"));
            assertNotNull(response.getDetailedBreachInfo().get("Adobe"));

            final RecordedRequest request = mockServer.takeRequest();
            assertEquals("POST", request.getMethod());
            assertEquals("/v1/domain-breaches", request.getPath());
            assertEquals("test-api-key", request.getHeader("x-api-key"));
        }
    }
}
