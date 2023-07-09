package org.metamechanists.quaptics.implementation.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;


public class LaserPointer extends QuapticChargeableItem {
    public static final Settings LASER_POINTER_SETTINGS = Settings.builder()
            .chargeCapacity(1000.0)
            .emissionPower(5.0)
            .build();
    public static final SlimefunItemStack LASER_POINTER = new SlimefunItemStack(
            "QP_LASER_POINTER",
            Material.BLACK_CANDLE,
            "&fLaser Pointer",
            Lore.buildChargeableLore(LASER_POINTER_SETTINGS, 0,
                    "&7● &eRight Click &7to toggle the pointer",
                    "&7● &eShift Right Click &7to change the color"));

    public LaserPointer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }
}
