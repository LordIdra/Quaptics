package org.metamechanists.death_lasers;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.implementation.emitters.LinearTimeEmitter;
import org.metamechanists.death_lasers.implementation.tools.TargetingWand;
import org.metamechanists.death_lasers.implementation.emitters.LinearVelocityEmitter;
import org.metamechanists.death_lasers.implementation.emitters.OscillatingLinearTimeEmitter;
import org.metamechanists.death_lasers.implementation.emitters.ShrinkingLinearTimeEmitter;

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
    public static final LinearVelocityEmitter linearVelocityEmitter = new LinearVelocityEmitter(
            Groups.DEATH_LASER_GROUP,
            LINEAR_VELOCITY_EMITTER,
            RecipeType.NULL,
            new ItemStack[] {},
            1000,
            100);
    public static final ShrinkingLinearTimeEmitter shrinkingLinearTimeEmitter = new ShrinkingLinearTimeEmitter(
            Groups.DEATH_LASER_GROUP,
            SHRINKING_LINEAR_TIME_EMITTER,
            RecipeType.NULL,
            new ItemStack[] {},
            1000,
            100);
    public static final OscillatingLinearTimeEmitter oscillatingLinearTimeEmitter = new OscillatingLinearTimeEmitter(
            Groups.DEATH_LASER_GROUP,
            OSCILLATING_LINEAR_TIME_EMITTER,
            RecipeType.NULL,
            new ItemStack[] {},
            1000,
            100);

    public static void initialize() {
        final SlimefunAddon addon = DEATH_LASERS.getInstance();

        targetingWand.register(addon);
        linearTimeEmitter.register(addon);
        linearVelocityEmitter.register(addon);
        shrinkingLinearTimeEmitter.register(addon);
        oscillatingLinearTimeEmitter.register(addon);
    }
}
