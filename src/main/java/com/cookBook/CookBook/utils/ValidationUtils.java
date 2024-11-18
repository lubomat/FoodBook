package com.cookBook.CookBook.utils;

import java.util.Arrays;
import java.util.List;

public class ValidationUtils {

    private static final List<String> FORBIDDEN_WORDS = Arrays.asList(
            "kurwa", "chuj", "cwel", "skurwiel","pierdol","jebany",
            "pizda","ciul","kutas","frajer","zjeb","debil","idiot",
            "dupa","gnoj","huj","spierdalaj","pojeb","gowno","lamus",
            "skurwysyn","zajebisty","wyjebany","szczoch","spierdolina"
    );

    private static final String POLISH_CHARACTERS_REGEX = ".*[ąćęłńóśźżĄĆĘŁŃÓŚŹŻ].*";

    public static boolean containsPolishCharacters(String text) {
        return text.matches(POLISH_CHARACTERS_REGEX);
    }

    public static boolean containsForbiddenWords(String text) {
        String lowerCaseText = text.toLowerCase();
        return FORBIDDEN_WORDS.stream().anyMatch(lowerCaseText::contains);
    }
}
