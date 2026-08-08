package com.xposedornot.examples;

import com.xposedornot.XposedOrNot;
import com.xposedornot.models.EmailBreachDetailedResponse;
import com.xposedornot.models.Breach;
import com.xposedornot.exceptions.XposedOrNotException;

public class PlusApiExample {
    public static void main(String[] args) {
        String apiKey = System.getenv("XON_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Set XON_API_KEY environment variable");
            System.exit(1);
        }

        try (XposedOrNot client = XposedOrNot.builder().apiKey(apiKey).build()) {

            // Plus API returns detailed breach information
            EmailBreachDetailedResponse result = client.email().checkDetailed("test@example.com");
            System.out.println("Status: " + result.getStatus());
            for (Breach breach : result.getBreaches()) {
                System.out.printf("  Breach: %s (Domain: %s, Records: %d)%n",
                    breach.getBreachId(), breach.getDomain(), breach.getXposedRecords());
            }

        } catch (XposedOrNotException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
