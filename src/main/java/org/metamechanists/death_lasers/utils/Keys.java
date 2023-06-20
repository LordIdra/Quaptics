package org.metamechanists.death_lasers.utils;

import org.bukkit.NamespacedKey;
import org.metamechanists.death_lasers.DEATH_LASERS;

public class Keys {
    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(DEATH_LASERS.getInstance(), key);
    }

    public static final NamespacedKey MAIN_GROUP = newKey("DEATH_LASER_GROUP");

    public static final NamespacedKey SOURCE = newKey("SOURCE");
    public static final NamespacedKey LOCATION_X = newKey("LOCATION_X");
    public static final NamespacedKey LOCATION_Y = newKey("LOCATION_Y");
    public static final NamespacedKey LOCATION_Z = newKey("LOCATION_Z");

}
