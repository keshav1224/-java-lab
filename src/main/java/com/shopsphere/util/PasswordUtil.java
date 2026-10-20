package com.shopsphere.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class PasswordUtil {
    private static final int COST = 10;
    private PasswordUtil() {}

    public static String hash(String plain) {
        return BCrypt.withDefaults().hashToString(COST, plain.toCharArray());
    }

    public static boolean verify(String plain, String hash) {
        if (plain == null || hash == null) return false;
        return BCrypt.verifyer().verify(plain.toCharArray(), hash).verified;
    }
}
