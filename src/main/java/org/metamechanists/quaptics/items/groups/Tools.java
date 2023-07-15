package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWand;
import org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun;
import org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun;
import org.metamechanists.quaptics.implementation.tools.targetingwand.TargetingWand;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWand.MULTIBLOCK_WAND;
import static org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun.RAY_GUN_2;
import static org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun.RAY_GUN_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun.RAY_GUN_1;
import static org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun.RAY_GUN_1_SETTINGS;
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

        new ModulatedRayGun(
                Groups.TOOLS,
                RAY_GUN_1,
                RecipeType.NULL,
                new ItemStack[]{},
                RAY_GUN_1_SETTINGS).register(addon);

        new DirectRayGun(
                Groups.TOOLS,
                RAY_GUN_2,
                RecipeType.NULL,
                new ItemStack[]{},
                RAY_GUN_2_SETTINGS).register(addon);
    }
}
