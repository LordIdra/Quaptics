package org.metamechanists.quaptics.utils;

import org.bukkit.NamespacedKey;
import org.metamechanists.quaptics.Quaptics;

public class Keys {
    public static NamespacedKey newKey(String key) {
        return new NamespacedKey(Quaptics.getInstance(), key);
    }

    public static final NamespacedKey MAIN_GROUP = newKey("QUAPTICS_GROUP");
    public static final NamespacedKey SOURCE = newKey("SOURCE");
    public static final NamespacedKey QUAPTICS_DATA = newKey("QUAPTICS_DATA");
    public static final NamespacedKey ATTRIBUTE_HIDDEN = newKey("ATTRIBUTE_HIDDEN");
    public static final NamespacedKey CONNECTION_POINT_ID = newKey("CONNECTION_POINT_ID");
    public static final String CONNECTION_GROUP_ID = "CONNECTION_GROUP_ID";
    public static final String POWERED = "POWERED";
    public static final String TARGET = "TARGET";
}
