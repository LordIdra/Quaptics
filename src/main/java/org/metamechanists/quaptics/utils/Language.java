package org.metamechanists.quaptics.utils;

import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.metalib.language.LanguageStorage;

public class Language {
    private static LanguageStorage languageStorage;

    public static void initialize() {
        languageStorage = new LanguageStorage(Quaptics.getInstance());
    }

    public static String getLanguageEntry(String path, Object... args) {
        return languageStorage.getLanguageEntry(path, args);
    }
}
