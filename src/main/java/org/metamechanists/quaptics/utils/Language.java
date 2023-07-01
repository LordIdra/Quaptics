package org.metamechanists.quaptics.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.metalib.language.LanguageStorage;

@UtilityClass
public class Language {
    private LanguageStorage languageStorage;

    public void initialize() {
        languageStorage = new LanguageStorage(Quaptics.getInstance());
    }

    public String getLanguageEntry(final String path, final Object... args) {
        return languageStorage != null ? languageStorage.getLanguageEntry(path, args) : "";
    }

    public void sendLanguageMessage(final Player player, final String path, final Object... args) {
        if (languageStorage != null) {
            languageStorage.sendLanguageMessage(player, path, args);
        }
    }
}
