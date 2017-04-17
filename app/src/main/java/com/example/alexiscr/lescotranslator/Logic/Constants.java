package com.example.alexiscr.lescotranslator.Logic;

/**
 * Class for Constants.
 */
public abstract class Constants {

    // Delays:
    public static final long SPLASH_SCREEN_DELAY = 1000;
    public static final long IMAGE_DELAY = 1500;

    // Links:
    static final String WEB_SERVICE_LINK =
            "https://lesco-translator-web-services.herokuapp.com/php-logic/lesco-objects.php?word=";

    // Strings:
    static final String WITH_ACCENT_MARKS = "áéíóú";
    static final String WITHOUT_ACCENT_MARKS = "aeiou";
    static final String ALPHABET_AND_NUMBERS = "abcdefghijklmnñopqrstuüvwxyz0123456789";
}
