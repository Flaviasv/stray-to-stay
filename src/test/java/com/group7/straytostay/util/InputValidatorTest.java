package com.group7.straytostay.util;

import com.group7.straytostay.model.Species;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputValidatorTest {

    @Test
    void email() {
        assertTrue(InputValidator.isValidEmail("ali@example.com"));
        assertFalse(InputValidator.isValidEmail("not an email"));
        assertFalse(InputValidator.isValidEmail(null));
    }

    @Test
    void password() {
        assertTrue(InputValidator.isValidPassword("Password1"));
        assertFalse(InputValidator.isValidPassword("short1"));
        assertFalse(InputValidator.isValidPassword("onlyletters"));
        assertFalse(InputValidator.isValidPassword("12345678"));
    }

    @Test
    void cleanKeepsSafeTextOnly() {
        assertEquals("Beagle", InputValidator.clean("  Beagle ", 80));
        assertEquals("St. John's", InputValidator.clean("St. John's", 80));
        assertNull(InputValidator.clean("<script>", 80));
        assertNull(InputValidator.clean("", 80));
        assertNull(InputValidator.clean("x".repeat(81), 80));
    }

    @Test
    void parsersFallBackInsteadOfThrowing() {
        assertEquals(Species.CAT, InputValidator.parseEnum(Species.class, "cat", null));
        assertNull(InputValidator.parseEnum(Species.class, "dragon", null));
        assertEquals(7, InputValidator.parseInt("7", 1));
        assertEquals(1, InputValidator.parseInt("seven", 1));
        assertNull(InputValidator.parseInteger("x"));
        assertEquals(Boolean.TRUE, InputValidator.parseBoolean("true"));
        assertNull(InputValidator.parseBoolean("maybe"));
        assertEquals(-1, InputValidator.parseLong("abc", -1));
    }
}
