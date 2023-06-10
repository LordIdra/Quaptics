package org.metamechanists.death_lasers;

import org.bukkit.NamespacedKey;

public class Keys {
    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(DEATH_LASERS.getInstance(), key);
    }

    public static final NamespacedKey MAIN_GROUP = newKey("DEATH_LASER_GROUP");

    public static final NamespacedKey TARGETING_MODE = newKey("TARGETING_MODE");
    public static final NamespacedKey LOCATION_WORLD = newKey("LOCATION_WORLD");
    public static final NamespacedKey LOCATION_X = newKey("LOCATION_X");
    public static final NamespacedKey LOCATION_Y = newKey("LOCATION_Y");
    public static final NamespacedKey LOCATION_Z = newKey("LOCATION_Z");
}
