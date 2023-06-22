package org.metamechanists.death_lasers.utils;

import org.bukkit.NamespacedKey;
import org.metamechanists.death_lasers.DEATH_LASERS;

public class Keys {
    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(DEATH_LASERS.getInstance(), key);
    }

    public static final NamespacedKey MAIN_GROUP = newKey("DEATH_LASER_GROUP");
    public static final NamespacedKey SOURCE = newKey("SOURCE");
    public static final NamespacedKey CONNECTION_POINT_ID = newKey("CONNECTION_POINT_ID");
    public static final String CONNECTION_GROUP_ID = "CONNECTION_GROUP_ID";
    public static final String POWERED = "POWERED";
    public static final String TARGET = "TARGET";

}
