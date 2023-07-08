package org.metamechanists.quaptics.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;

@UtilityClass
public class Keys {
    @Contract("_ -> new")
    private @NotNull NamespacedKey newKey(final String key) {
        return new NamespacedKey(Quaptics.getInstance(), key);
    }

    public final NamespacedKey RECIPE_INFUSION_CONTAINER = newKey("RECIPE_INFUSION_CONTAINER");
    public final NamespacedKey MAIN = newKey("MAIN");
    public final NamespacedKey GUIDE = newKey("GUIDE");
    public final NamespacedKey TOOLS = newKey("TOOLS");
    public final NamespacedKey PRIMITIVE = newKey("PRIMITIVE");
    public final NamespacedKey BASIC = newKey("BASIC");
    public final NamespacedKey INTERMEDIATE = newKey("INTERMEDIATE");
    public final NamespacedKey ADVANCED = newKey("ADVANCED");
    public final NamespacedKey TESTING = newKey("TESTING");
    public final NamespacedKey FACING = newKey("FACING");
    public final NamespacedKey SOURCE = newKey("SOURCE");
    public final NamespacedKey CHARGE = newKey("CHARGE");

    public final String BS_PLAYER = "QP_PLAYER";
    public final String BS_FACING = "QP_FACING";
    public final String BS_PANEL_ID = "QP_PANEL_ID";
    public final String BS_BURNOUT = "QP_BURNOUT";
    public final String BS_PROGRESS = "QP_PROGRESS";
    public final String BS_CHARGE = "QP_CHARGE";
    public final String BS_CHARGE_RATE = "QP_CHARGE_RATE";
    public final String BS_POWERED = "QP_POWERED";
    public final String BS_ENOUGH_ENERGY = "QP_ENOUGH_ENERGY";
    public final String BS_ENABLED = "QP_ENABLED";
    public final String BS_TARGET = "QP_TARGET";
    public final String BS_VELOCITY = "QP_VELOCITY";
    public final String BS_TICKS_SINCE_LAST_UPDATE = "QP_TICKS_SINCE_LAST_UPDATE";
    public final String BS_IS_HOLDING_ITEM = "QP_IS_HOLDING_ITEM";
    public final String BS_HEIGHT = "QP_HEIGHT";
    public final String BS_SIZE = "QP_SIZE";
    public final String BS_MODE = "QP_MODE";
    public final String BS_CRAFT_IN_PROGRESS = "QP_CRAFT_IN_PROGRESS";
    public final String BS_SECONDS_SINCE_CRAFT_STARTED = "QP_TIME_SINCE_CRAFT_STARTED";
}
