package com.group7.straytostay.util;

import java.util.regex.Pattern;

/**
 * Whitelist validation and safe parsing for request parameters.
 * Parsing never throws: a bad value becomes the given default.
 */
public final class InputValidator {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern SAFE_TEXT = Pattern.compile("^[\\p{L}\\p{N} .,'()&/-]*$");

    private InputValidator() { }

    public static boolean isValidEmail(String email) {
        return email != null && email.length() <= 254 && EMAIL.matcher(email).matches();
    }

    /** At least 8 characters with one letter and one digit. */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 72) {
            return false;
        }
        boolean letter = false;
        boolean digit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) letter = true;
            if (Character.isDigit(c)) digit = true;
        }
        return letter && digit;
    }

    /**
     * Trims the value and keeps it only if it is made of letters, digits and basic punctuation.
     * Returns null for empty, too long or unsafe values.
     */
    public static String clean(String value, int maxLength) {
        if (value == null) return null;
        String t = value.trim();
        if (t.isEmpty() || t.length() > maxLength || !SAFE_TEXT.matcher(t).matches()) {
            return null;
        }
        return t;
    }

    public static <E extends Enum<E>> E parseEnum(Class<E> type, String value, E fallback) {
        if (value == null || value.isBlank()) return fallback;
        try {
            return Enum.valueOf(type, value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return fallback;
        }
    }

    public static int parseInt(String value, int fallback) {
        if (value == null || value.isBlank()) return fallback;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Boolean parseBoolean(String value) {
        if (value == null || value.isBlank()) return null;
        String v = value.trim().toLowerCase();
        if (v.equals("true") || v.equals("yes") || v.equals("1") || v.equals("on")) return Boolean.TRUE;
        if (v.equals("false") || v.equals("no") || v.equals("0") || v.equals("off")) return Boolean.FALSE;
        return null;
    }

    public static long parseLong(String value, long fallback) {
        if (value == null || value.isBlank()) return fallback;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
