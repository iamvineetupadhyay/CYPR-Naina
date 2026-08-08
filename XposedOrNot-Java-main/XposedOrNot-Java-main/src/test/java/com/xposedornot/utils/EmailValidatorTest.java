package com.xposedornot.utils;

import com.xposedornot.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the EmailValidator utility.
 */
class EmailValidatorTest {

    @Test
    void validEmailPasses() {
        assertDoesNotThrow(() -> EmailValidator.validate("user@example.com"));
    }

    @Test
    void validEmailWithSubdomainPasses() {
        assertDoesNotThrow(() -> EmailValidator.validate("user@mail.example.com"));
    }

    @Test
    void validEmailWithPlusPasses() {
        assertDoesNotThrow(() -> EmailValidator.validate("user+tag@example.com"));
    }

    @Test
    void validEmailWithDotsPasses() {
        assertDoesNotThrow(() -> EmailValidator.validate("first.last@example.com"));
    }

    @Test
    void nullEmailThrows() {
        assertThrows(ValidationException.class, () -> EmailValidator.validate(null));
    }

    @Test
    void emptyEmailThrows() {
        assertThrows(ValidationException.class, () -> EmailValidator.validate(""));
    }

    @Test
    void blankEmailThrows() {
        assertThrows(ValidationException.class, () -> EmailValidator.validate("   "));
    }

    @Test
    void emailWithoutAtThrows() {
        assertThrows(ValidationException.class, () -> EmailValidator.validate("userexample.com"));
    }

    @Test
    void emailWithoutDomainThrows() {
        assertThrows(ValidationException.class, () -> EmailValidator.validate("user@"));
    }

    @Test
    void emailWithoutTldThrows() {
        assertThrows(ValidationException.class, () -> EmailValidator.validate("user@example"));
    }

    @Test
    void emailWithSpacesThrows() {
        assertThrows(ValidationException.class, () -> EmailValidator.validate("user @example.com"));
    }
}
