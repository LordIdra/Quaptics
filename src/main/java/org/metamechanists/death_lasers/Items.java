package org.metamechanists.death_lasers;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.implementation.emitters.LinearTimeEmitter;
import org.metamechanists.death_lasers.implementation.tools.TargetingWand;
import org.metamechanists.death_lasers.implementation.emitters.LinearVelocityEmitter;
import org.metamechanists.death_lasers.implementation.emitters.OscillatingLinearTimeEmitter;
import org.metamechanists.death_lasers.implementation.emitters.ShrinkingLinearTimeEmitter;

public class Items {
    public static final SlimefunItemStack LINEAR_TIME_EMITTER = new SlimefunItemStack(
            "LINEAR_TIME_EMITTER",
            Material.GLASS,
            "&4&lLinear Time Emitter");
    public static final SlimefunItemStack LINEAR_VELOCITY_EMITTER = new SlimefunItemStack(
            "LINEAR_VELOCITY_EMITTER",
            Material.GLASS,
            "&4&lLinear Velocity Emitter");
    public static final SlimefunItemStack SHRINKING_LINEAR_TIME_EMITTER = new SlimefunItemStack(
            "SHRINKING_LINEAR_TIME_EMITTER",
            Material.GLASS,
            "&4&lShrinking Linear Time Emitter");
    public static final SlimefunItemStack OSCILLATING_LINEAR_TIME_EMITTER = new SlimefunItemStack(
            "OSCILLATING_LINEAR_TIME_EMITTER",
            Material.GLASS,
            "&4&lOscillating Linear Time Emitter");
    public static final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "TARGETING_WAND",
            Material.BLAZE_ROD,
            "&6Targeting Wand");

    public static final TargetingWand targetingWand = new TargetingWand(
            Groups.DEATH_LASER_GROUP,
            TARGETING_WAND,
            RecipeType.NULL,
            new ItemStack[] {});
    public static void initialize() {
        final SlimefunAddon addon = DEATH_LASERS.getInstance();
        targetingWand.register(addon);

        new LinearTimeEmitter(
                Groups.DEATH_LASER_GROUP,
                LINEAR_TIME_EMITTER,
                RecipeType.NULL,
                new ItemStack[] {},
                1000,
                100)
                .register(addon);

        new LinearVelocityEmitter(
                Groups.DEATH_LASER_GROUP,
                LINEAR_VELOCITY_EMITTER,
                RecipeType.NULL,
                new ItemStack[] {},
                1000,
                100)
                .register(addon);

        new ShrinkingLinearTimeEmitter(
                Groups.DEATH_LASER_GROUP,
                SHRINKING_LINEAR_TIME_EMITTER,
                RecipeType.NULL,
                new ItemStack[] {},
                1000,
                100)
                .register(addon);

        new OscillatingLinearTimeEmitter(
                Groups.DEATH_LASER_GROUP,
                OSCILLATING_LINEAR_TIME_EMITTER,
                RecipeType.NULL,
                new ItemStack[] {},
                1000,
                100)
                .register(addon);
    }
}
