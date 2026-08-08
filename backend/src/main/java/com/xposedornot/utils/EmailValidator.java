package com.xposedornot.utils;

import com.xposedornot.exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * Utility class for validating email addresses.
 */
public final class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    );

    private EmailValidator() {
        // Utility class
    }

    /**
     * Validates that the given string is a well-formed email address.
     *
     * @param email the email address to validate
     * @throws ValidationException if the email is null, empty, or malformed
     */
    public static void validate(final String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email address must not be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("Invalid email address format");
        }
    }
}
