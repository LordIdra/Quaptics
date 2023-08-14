package org.metamechanists.aircraft.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.aircraft.items.Groups;

@UtilityClass
public class Aircraft {
    public void initialize() {
        final SlimefunAddon addon = org.metamechanists.aircraft.Aircraft.getInstance();

        new Glider(Groups.AIRCRAFT, GLIDER, RecipeType.NULL, new ItemStack[]{}).register(addon);
    }
}
