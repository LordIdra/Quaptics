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

    public static final NamespacedKey MAIN = newKey("MAIN");
    public static final NamespacedKey GUIDE = newKey("GUIDE");
    public static final NamespacedKey TOOLS = newKey("TOOLS");
    public static final NamespacedKey PRIMITIVE = newKey("PRIMITIVE");
    public static final NamespacedKey BASIC = newKey("BASIC");
    public static final NamespacedKey INTERMEDIATE = newKey("INTERMEDIATE");
    public static final NamespacedKey ADVANCED = newKey("ADVANCED");

    public static final NamespacedKey FACING = newKey("FACING");
    public static final NamespacedKey SOURCE = newKey("SOURCE");
    public static final NamespacedKey DATA = newKey("DATA");

    public static final String BURNOUT = "QP_BURNOUT";
    public static final String CHARGE = "QP_CHARGE";
    public static final String POWERED = "QP_POWERED";
    public static final String TARGET = "QP_TARGET";
}
