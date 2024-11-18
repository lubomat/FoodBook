package com.cookBook.CookBook.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testContainsPolishCharacters() {
        assertTrue(ValidationUtils.containsPolishCharacters("Łukasz"));
        assertFalse(ValidationUtils.containsPolishCharacters("Lukasz"));
    }

    @Test
    void testContainsForbiddenWords() {
        assertTrue(ValidationUtils.containsForbiddenWords("To jest kurwa przykład"));
        assertFalse(ValidationUtils.containsForbiddenWords("To jest przykład"));
    }
}
