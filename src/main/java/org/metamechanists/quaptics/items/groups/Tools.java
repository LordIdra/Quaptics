package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.tools.LaserPointer;
import org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWand;
import org.metamechanists.quaptics.implementation.tools.targetingwand.TargetingWand;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.tools.LaserPointer.LASER_POINTER;
import static org.metamechanists.quaptics.implementation.tools.LaserPointer.LASER_POINTER_SETTINGS;
import static org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWand.MULTIBLOCK_WAND;
import static org.metamechanists.quaptics.implementation.tools.targetingwand.TargetingWand.TARGETING_WAND;


@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Tools {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new TargetingWand(
                Groups.TOOLS,
                TARGETING_WAND,
                RecipeType.NULL,
                new ItemStack[]{}).register(addon);

        new MultiblockWand(
                Groups.TOOLS,
                MULTIBLOCK_WAND,
                RecipeType.NULL,
                new ItemStack[]{}).register(addon);

        new LaserPointer(
                Groups.TOOLS,
                LASER_POINTER,
                RecipeType.NULL,
                new ItemStack[]{},
                LASER_POINTER_SETTINGS).register(addon);
    }
}
