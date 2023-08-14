package org.metamechanists.aircraft.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.Aircraft;

@UtilityClass
public class Keys {
    private @NotNull NamespacedKey newKey(final String key) {
        return new NamespacedKey(Aircraft.getInstance(), key);
    }

    public final NamespacedKey AIRCRAFT = newKey("AIRCRAFT");
}
