package org.metamechanists.quaptics.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.metalib.language.LanguageStorage;

@UtilityClass
public class Language {
    private final LanguageStorage languageStorage = new LanguageStorage(Quaptics.getInstance());

    public String getLanguageEntry(final String path, final Object... args) {
        return languageStorage.getLanguageEntry(path, args);
    }

    public void sendLanguageMessage(final Player player, final String path, final Object... args) {
        languageStorage.sendLanguageMessage(player, path, args);
    }
}
