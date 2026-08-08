package com.xposedornot.examples;

import com.xposedornot.XposedOrNot;
import com.xposedornot.models.EmailBreachResponse;
import com.xposedornot.models.BreachInfo;
import com.xposedornot.models.PasswordCheckResponse;
import com.xposedornot.exceptions.XposedOrNotException;

import java.util.List;

public class BasicExample {
    public static void main(String[] args) {
        try (XposedOrNot client = XposedOrNot.builder().build()) {

            // Check if an email has been exposed
            EmailBreachResponse result = client.email().check("test@example.com");
            System.out.println("Found in " + result.getBreachNames().size() + " breaches");

            // Get all known breaches
            List<BreachInfo> breaches = client.breaches().list();
            System.out.println("Total known breaches: " + breaches.size());

            // Check a password (hashed locally, never sent in clear text)
            PasswordCheckResponse passResult = client.password().check("password123");
            System.out.println("Password found " + passResult.getExposureCount() + " times");

        } catch (XposedOrNotException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
