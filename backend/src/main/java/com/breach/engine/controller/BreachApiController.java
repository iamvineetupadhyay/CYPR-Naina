package com.breach.engine.controller;

import com.breach.engine.model.PublicBreachRecord;
import com.breach.engine.repository.PublicBreachRepository;
import com.breach.engine.service.FreeSecurityApiService;
import com.breach.engine.service.PublicBreachScraperService;
import com.breach.engine.service.RateLimitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class BreachApiController {

    @Autowired
    private PublicBreachRepository breachRepository;

    @Autowired
    private FreeSecurityApiService freeSecurityApiService;

    @Autowired
    private PublicBreachScraperService scraperService;

    @Autowired
    private RateLimitingService rateLimitingService;

    /**
     * Rate Limit Status Endpoint
     */
    @GetMapping("/user/rate-limit")
    public ResponseEntity<Map<String, Object>> getRateLimitStatus(
            @RequestParam(defaultValue = "default_user") String userKey,
            @RequestParam(defaultValue = "XPOSEDORNOT") String engineMode) {
        Map<String, Object> status = rateLimitingService.checkAndConsumeQuota(userKey, engineMode);
        return ResponseEntity.ok(status);
    }

    /**
     * Search Identity (Email / Username) with XON SDK vs Local Engine Toggle
     */
    @GetMapping("/breach/search")
    public ResponseEntity<Map<String, Object>> searchIdentity(
            @RequestParam String query,
            @RequestParam(defaultValue = "default_user") String userKey,
            @RequestParam(defaultValue = "XPOSEDORNOT") String engineMode) {

        Map<String, Object> quota = rateLimitingService.checkAndConsumeQuota(userKey, engineMode);
        if (!Boolean.TRUE.equals(quota.get("allowed"))) {
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("error", "XposedOrNot SDK Rate Limit Exceeded (Max 10 req/min). Switch to Local Engine for unlimited searches!");
            errorResp.put("rateLimit", quota);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResp);
        }

        Map<String, Object> result;
        if ("LOCAL".equalsIgnoreCase(engineMode)) {
            // Local H2 DB Search
            List<PublicBreachRecord> matches = breachRepository.findAll().stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            b.getDomain().toLowerCase().contains(query.toLowerCase()) ||
                            b.getDescription().toLowerCase().contains(query.toLowerCase()))
                    .toList();

            result = new HashMap<>();
            result.put("query", query);
            result.put("isExposed", !matches.isEmpty());
            result.put("exposureCount", matches.size());
            result.put("breaches", matches);
            result.put("dataSource", "Local Engine H2 Database");
        } else {
            // XposedOrNot Global SDK
            result = freeSecurityApiService.searchIdentityExposure(query);
            result.put("dataSource", "XposedOrNot Global Java SDK");
        }

        result.put("rateLimit", quota);
        return ResponseEntity.ok(result);
    }

    /**
     * Check Password Exposure via free k-Anonymity HIBP API
     */
    @PostMapping("/password/check")
    public ResponseEntity<Map<String, Object>> checkPassword(
            @RequestBody Map<String, String> body,
            @RequestParam(defaultValue = "default_user") String userKey) {
        Map<String, Object> quota = rateLimitingService.checkAndConsumeQuota(userKey, "HIBP_HIGH_CAPACITY");

        String password = body.getOrDefault("password", "");
        if (password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password field is required"));
        }
        Map<String, Object> result = freeSecurityApiService.checkPasswordPwnedStatus(password);
        result.put("rateLimit", quota);
        return ResponseEntity.ok(result);
    }

    /**
     * Domain Security Audit
     */
    @GetMapping("/domain/audit")
    public ResponseEntity<Map<String, Object>> auditDomain(
            @RequestParam String domain,
            @RequestParam(defaultValue = "default_user") String userKey) {
        Map<String, Object> quota = rateLimitingService.checkAndConsumeQuota(userKey, "HIBP_HIGH_CAPACITY");
        Map<String, Object> result = freeSecurityApiService.auditDomainSecurity(domain);
        result.put("rateLimit", quota);
        return ResponseEntity.ok(result);
    }

    /**
     * Get All Scraped & Indexed Public Breaches
     */
    @GetMapping("/breaches/all")
    public ResponseEntity<List<PublicBreachRecord>> getAllBreaches() {
        return ResponseEntity.ok(breachRepository.findAll());
    }

    /**
     * Trigger Public Web Scraper on demand
     */
    @PostMapping("/scraper/run")
    public ResponseEntity<Map<String, Object>> triggerScraper() {
        List<PublicBreachRecord> newScraped = scraperService.scrapeWikipediaBreaches();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("newRecordsScraped", newScraped.size());
        result.put("totalDatabaseRecords", breachRepository.count());
        result.put("message", "Public web scraper finished successfully.");
        return ResponseEntity.ok(result);
    }

    /**
     * System Stats & Metrics - 100% Dynamically Calculated from H2 Database
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        // Ensure initial seeding if DB is empty
        if (breachRepository.count() == 0) {
            scraperService.scrapeWikipediaBreaches();
        }

        long totalBreaches = breachRepository.count();
        long dbTotalExposedAccounts = breachRepository.findAll().stream()
                .mapToLong(b -> b.getPwnCount() != null ? b.getPwnCount() : 0)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("indexedBreaches", totalBreaches);
        stats.put("totalExposedAccounts", dbTotalExposedAccounts);
        stats.put("engineStatus", "ONLINE");
        stats.put("activeScraper", "READY");
        stats.put("sdkRateLimit", "10 Req / Min (XposedOrNot Free Tier)");

        return ResponseEntity.ok(stats);
    }
}
