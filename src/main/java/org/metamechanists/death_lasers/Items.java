package org.metamechanists.death_lasers;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.implementation.emitters.LinearTimeEmitter;
import org.metamechanists.death_lasers.implementation.tools.TargetingWand;

import static org.metamechanists.death_lasers.ItemStacks.*;

public class Items {
    public static final TargetingWand targetingWand = new TargetingWand(
            Groups.DEATH_LASER_GROUP,
            TARGETING_WAND,
            RecipeType.NULL,
            new ItemStack[] {});
    public static final LinearTimeEmitter linearTimeEmitter = new LinearTimeEmitter(
            Groups.DEATH_LASER_GROUP,
            LINEAR_TIME_EMITTER,
            RecipeType.NULL,
            new ItemStack[] {},
            1000,
            100);

    public static void initialize() {
        final SlimefunAddon addon = DEATH_LASERS.getInstance();

        targetingWand.register(addon);
        linearTimeEmitter.register(addon);
    }
}
