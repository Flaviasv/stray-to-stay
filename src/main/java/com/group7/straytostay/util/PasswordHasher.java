package com.group7.straytostay.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * BCrypt hashing. The salt is stored inside the hash string, so only the hash is saved.
 */
public final class PasswordHasher {

    private static final int COST = 12;

    private PasswordHasher() { }

    public static String hash(String plainPassword) {
        return BCrypt.withDefaults().hashToString(COST, plainPassword.toCharArray());
    }

    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), storedHash).verified;
    }
}
