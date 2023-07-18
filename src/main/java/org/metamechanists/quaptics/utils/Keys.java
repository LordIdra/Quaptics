package org.metamechanists.quaptics.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;

@UtilityClass
public class Keys {
    private @NotNull NamespacedKey newKey(final String key) {
        return new NamespacedKey(Quaptics.getInstance(), key);
    }

    public final NamespacedKey RECIPE_INFUSION_CONTAINER = newKey("RECIPE_INFUSION_CONTAINER");
    public final NamespacedKey RECIPE_ENTANGLER = newKey("RECIPE_ENTANGLER");
    public final NamespacedKey RECIPE_CRYSTAL_REFINER = newKey("RECIPE_CRYSTAL_REFINER");
    public final NamespacedKey MAIN = newKey("MAIN");
    public final NamespacedKey GUIDE = newKey("GUIDE");
    public final NamespacedKey TOOLS = newKey("TOOLS");
    public final NamespacedKey BEAM_CREATION = newKey("BEAM_CREATION");
    public final NamespacedKey BEAM_MANIPULATION = newKey("BEAM_MANIPULATION");
    public final NamespacedKey FREQUENCY_AND_PHASE = newKey("FREQUENCY_AND_PHASE");
    public final NamespacedKey BEACON_COMPONENTS = newKey("BEACON_COMPONENTS");
    public final NamespacedKey BEACON_MODULES = newKey("BEACON_MODULES");
    public final NamespacedKey MACHINES = newKey("MACHINES");
    public final NamespacedKey TESTING = newKey("TESTING");
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
    public final String BS_MULTIBLOCK_INTACT = "QP_MULTIBLOCK_INTACT";
    public final String BS_CRAFT_IN_PROGRESS = "QP_CRAFT_IN_PROGRESS";
    public final String BS_SECONDS_SINCE_CRAFT_STARTED = "QP_TIME_SINCE_CRAFT_STARTED";
    public final String BS_SECONDS_SINCE_REACTOR_STARTED = "QP_TIME_SINCE_REACTOR_STARTED";
    public final String BS_INPUT_POWER = "QP_INPUT_POWER";
    public final String BS_OUTPUT_POWER = "QP_OUTPUT_POWER";
    public final String BS_ANIMATION_OFFSET = "QP_ANIMATION_OFFSET";
    public final String BS_INTERACTION_ID_LIST = "QP_INTERACTION_ID_LIST";
    public final String BS_PLAYER_RECEIVERS = "QP_PLAYER_RECEIVERS";
}
