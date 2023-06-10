package org.metamechanists.death_lasers.utils;

import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.metalib.language.LanguageStorage;

public class Language {
    private static LanguageStorage languageStorage;

    public static void initialize() {
        languageStorage = new LanguageStorage(DEATH_LASERS.getInstance());
    }

    public static String getLanguageEntry(String path, Object... args) {
        return languageStorage.getLanguageEntry(path, args);
    }
}
