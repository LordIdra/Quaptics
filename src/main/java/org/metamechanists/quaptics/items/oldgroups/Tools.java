package org.metamechanists.quaptics.items.oldgroups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWand;
import org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun;
import org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun;
import org.metamechanists.quaptics.implementation.tools.targetingwand.TargetingWand;
import org.metamechanists.quaptics.items.OldGroups;

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
                OldGroups.TOOLS,
                TARGETING_WAND,
                RecipeType.NULL,
                new ItemStack[]{}).register(addon);

        new MultiblockWand(
                OldGroups.TOOLS,
                MULTIBLOCK_WAND,
                RecipeType.NULL,
                new ItemStack[]{}).register(addon);

        new ModulatedRayGun(
                OldGroups.TOOLS,
                RAY_GUN_1,
                RecipeType.NULL,
                new ItemStack[]{},
                RAY_GUN_1_SETTINGS).register(addon);

        new DirectRayGun(
                OldGroups.TOOLS,
                RAY_GUN_2,
                RecipeType.NULL,
                new ItemStack[]{},
                RAY_GUN_2_SETTINGS).register(addon);
    }
}
