package org.metamechanists.quaptics.utils;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;

public class Keys {
    @Contract("_ -> new")
    public static @NotNull NamespacedKey newKey(String key) {
        return new NamespacedKey(Quaptics.getInstance(), key);
    }

    public static final NamespacedKey MAIN_GROUP = newKey("QUAPTICS_GROUP");
    public static final NamespacedKey SOURCE = newKey("SOURCE");
    public static final NamespacedKey QUAPTICS_DATA = newKey("QUAPTICS_DATA");
    public static final String CONNECTION_GROUP_ID = "CONNECTION_GROUP_ID";
    public static final String POWERED = "POWERED";
    public static final String TARGET = "TARGET";
}
