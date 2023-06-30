package org.metamechanists.quaptics.implementation.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;

public class LaserPointer extends QuapticChargeableItem {
    public LaserPointer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ConnectedBlock.Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }
}
