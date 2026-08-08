package com.xposedornot.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the KeccakHasher utility.
 *
 * <p>Tests verify that the original Keccak-512 algorithm is used (NOT SHA3-512).
 * The two algorithms differ in their padding scheme, producing different outputs
 * for the same input.</p>
 */
class KeccakHasherTest {

    @Test
    void hashProduces128HexCharacters() {
        final String hash = KeccakHasher.hash("test");
        assertNotNull(hash);
        assertEquals(128, hash.length());
        assertTrue(hash.matches("[0-9a-f]{128}"));
    }

    @Test
    void hashPrefixReturnsFirst10Characters() {
        final String hash = KeccakHasher.hash("test");
        final String prefix = KeccakHasher.hashPrefix("test");
        assertEquals(10, prefix.length());
        assertEquals(hash.substring(0, 10), prefix);
    }

    @Test
    void hashIsConsistent() {
        final String hash1 = KeccakHasher.hash("password123");
        final String hash2 = KeccakHasher.hash("password123");
        assertEquals(hash1, hash2);
    }

    @Test
    void differentInputsProduceDifferentHashes() {
        final String hash1 = KeccakHasher.hash("password1");
        final String hash2 = KeccakHasher.hash("password2");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void emptyStringProducesValidHash() {
        final String hash = KeccakHasher.hash("");
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void hashUsesOriginalKeccakNotSha3() {
        // The original Keccak-512 hash of empty string is different from SHA3-512.
        // Keccak-512("") starts with "0eab42de4c3ceb92"
        // SHA3-512("") starts with "a69f73cca23a9ac5"
        final String hash = KeccakHasher.hash("");
        assertTrue(hash.startsWith("0eab42de4c3ceb92"),
                "Hash should use original Keccak-512, not SHA3-512. Got: " + hash);
    }

    @Test
    void hashOfTestStringMatchesKnownValue() {
        // Known original Keccak-512 hash of "test"
        final String hash = KeccakHasher.hash("test");
        // Keccak-512("test") starts with "1e2e9fc2002b002d"
        assertTrue(hash.startsWith("1e2e9fc2002b002d"),
                "Keccak-512 of 'test' should match known value. Got: " + hash);
    }

    @Test
    void hashHandlesUnicodeInput() {
        final String hash = KeccakHasher.hash("p\u00e4ssw\u00f6rd");
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void hashHandlesSpecialCharacters() {
        final String hash = KeccakHasher.hash("p@$$w0rd!#%");
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }
}
