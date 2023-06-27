package org.metamechanists.quaptics.utils;

import org.bukkit.entity.Player;
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

    public static void sendLanguageMessage(Player player, String path, Object... args) {
        languageStorage.sendLanguageMessage(player, path, args);
    }
}
