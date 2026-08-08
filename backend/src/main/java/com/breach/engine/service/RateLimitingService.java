package com.breach.engine.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // Rate Limit Specs:
    // XposedOrNot SDK (Wider Data Set): 10 requests per minute (1 req / 2 sec)
    // Local Engine (H2 DB): Unlimited
    private static final int XON_MAX_REQUESTS_PER_MINUTE = 10;
    private static final long ONE_MINUTE_MILLIS = 60 * 1000L;

    private final Map<String, UserRateLimitWindow> rateLimitMap = new ConcurrentHashMap<>();

    public synchronized Map<String, Object> checkAndConsumeQuota(String userKey, String engineMode) {
        Map<String, Object> status = new HashMap<>();

        if ("LOCAL".equalsIgnoreCase(engineMode)) {
            status.put("allowed", true);
            status.put("maxLimit", "UNLIMITED");
            status.put("usedQuota", 0);
            status.put("remainingQuota", "UNLIMITED");
            status.put("resetInSeconds", 0);
            status.put("sdkRateLimit", "Local Engine H2 Database (Unlimited Scraped Disclosures)");
            status.put("engineMode", "LOCAL");
            return status;
        }

        long now = System.currentTimeMillis();
        UserRateLimitWindow window = rateLimitMap.computeIfAbsent(userKey, k -> new UserRateLimitWindow(now));

        if (now - window.startTime > ONE_MINUTE_MILLIS) {
            window.startTime = now;
            window.requestCount = 0;
        }

        boolean isAllowed = window.requestCount < XON_MAX_REQUESTS_PER_MINUTE;
        if (isAllowed) {
            window.requestCount++;
        }

        long secondsUntilReset = Math.max(1, (ONE_MINUTE_MILLIS - (now - window.startTime)) / 1000);
        int remainingQuota = Math.max(0, XON_MAX_REQUESTS_PER_MINUTE - window.requestCount);

        status.put("allowed", isAllowed);
        status.put("maxLimit", XON_MAX_REQUESTS_PER_MINUTE);
        status.put("usedQuota", window.requestCount);
        status.put("remainingQuota", remainingQuota);
        status.put("resetInSeconds", secondsUntilReset);
        status.put("sdkRateLimit", "XposedOrNot Global SDK (10 req/min - 1 req / 2s)");
        status.put("engineMode", "XPOSEDORNOT_SDK");

        return status;
    }

    private static class UserRateLimitWindow {
        long startTime;
        int requestCount;

        UserRateLimitWindow(long startTime) {
            this.startTime = startTime;
            this.requestCount = 0;
        }
    }
}
