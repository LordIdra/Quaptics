package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.implementation.tools.LaserPointer;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;

@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Tools {
    public final Settings LASER_POINTER_SETTINGS = Settings.builder()
            .capacity(1000.0)
            .emissionPower(5.0)
            .minFrequency(0.5)
            .maxFrequency(1.0)
            .build();

    public final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "QP_TARGETING_WAND",
            Material.BLAZE_ROD,
            "&6Targeting Wand",
            "&7● &eRight Click &7to select a source",
            "&7● &eRight Click &7again to create a link",
            "&7● &eShift Right Click &7to remove a link");

    public final SlimefunItemStack LASER_POINTER = new SlimefunItemStack(
            "QP_LASER_POINTER",
            Material.BLACK_CANDLE,
            "&fLaser Pointer",
            Lore.buildChargeableLore(LASER_POINTER_SETTINGS, 0,
                    "&7● &eRight Click &7to toggle the pointer",
                    "&7● &eShift Right Click &7to change the color"));

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new TargetingWand(
                Groups.TOOLS,
                TARGETING_WAND,
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
