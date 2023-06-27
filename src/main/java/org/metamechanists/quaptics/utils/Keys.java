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

    public static final NamespacedKey MAIN = newKey("QP_MAIN");
    public static final NamespacedKey GUIDE = newKey("QP_GUIDE");
    public static final NamespacedKey TOOLS = newKey("QP_TOOLS");
    public static final NamespacedKey PRIMITIVE = newKey("QP_PRIMITIVE");
    public static final NamespacedKey BASIC = newKey("QP_BASIC");
    public static final NamespacedKey INTERMEDIATE = newKey("QP_INTERMEDIATE");
    public static final NamespacedKey ADVANCED = newKey("QP_ADVANCED");
    
    public static final NamespacedKey SOURCE = newKey("QP_SOURCE");
    public static final NamespacedKey DATA = newKey("QP_DATA");
    public static final String CONNECTION_GROUP_ID = "QP_CONNECTION_GROUP_ID";
    public static final String POWERED = "QP_POWERED";
    public static final String TARGET = "QP_TARGET";
}
