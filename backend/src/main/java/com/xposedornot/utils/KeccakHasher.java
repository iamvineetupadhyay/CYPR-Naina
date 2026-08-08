package com.xposedornot.utils;

import org.bouncycastle.crypto.digests.KeccakDigest;

import java.nio.charset.StandardCharsets;

/**
 * Utility class for computing original Keccak-512 hashes (NOT SHA3-512 / FIPS 202).
 *
 * <p>Uses Bouncy Castle's {@link KeccakDigest} which implements the original Keccak
 * submission to the SHA-3 competition, with the original padding scheme (not the
 * NIST-standardized SHA-3 padding).</p>
 */
public final class KeccakHasher {

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private KeccakHasher() {
        // Utility class
    }

    /**
     * Computes the original Keccak-512 hash of the given password.
     *
     * @param password the password to hash
     * @return the full hex-encoded Keccak-512 digest (128 hex characters)
     */
    public static String hash(final String password) {
        final KeccakDigest digest = new KeccakDigest(512);
        final byte[] input = password.getBytes(StandardCharsets.UTF_8);
        digest.update(input, 0, input.length);
        final byte[] output = new byte[64];
        digest.doFinal(output, 0);
        return bytesToHex(output);
    }

    /**
     * Computes the Keccak-512 hash prefix (first 10 hex characters) for anonymous
     * password checking.
     *
     * @param password the password to hash
     * @return the first 10 hex characters of the Keccak-512 digest
     */
    public static String hashPrefix(final String password) {
        return hash(password).substring(0, 10);
    }

    private static String bytesToHex(final byte[] bytes) {
        final char[] hex = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            final int v = bytes[i] & 0xFF;
            hex[i * 2] = HEX_CHARS[v >>> 4];
            hex[i * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hex);
    }
}
