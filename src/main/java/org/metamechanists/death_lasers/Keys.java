package org.metamechanists.death_lasers;

import org.bukkit.NamespacedKey;

public class Keys {
    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(DEATH_LASERS.getInstance(), key);
    }

    public static final NamespacedKey MAIN_GROUP = newKey("DEATH_LASER_GROUP");
}
