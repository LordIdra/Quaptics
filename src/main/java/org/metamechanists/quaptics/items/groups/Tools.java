package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;
import org.metamechanists.quaptics.items.Groups;

public class Tools {
    public static final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "QP_TARGETING_WAND",
            Material.BLAZE_ROD,
            "&6Targeting Wand",
            "&7● &eRight Click &7to select a source",
            "&7● &eRight Click &7again to create a link",
            "&7● &eShift Right Click &7to remove a link");

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new TargetingWand(
                Groups.TOOLS,
                TARGETING_WAND,
                RecipeType.NULL,
                new ItemStack[]{}).register(addon);
    }
}
