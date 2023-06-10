package org.metamechanists.death_lasers;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.implementation.DeathLaser;
import org.metamechanists.death_lasers.implementation.TargetingWand;

public class Items {
    public static final SlimefunItemStack DEATH_LASER = new SlimefunItemStack(
            "DEATH_LASER",
            Material.GLASS,
            "&4&lDeath Laser");
    public static final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "TARGETING_WAND",
            Material.GLASS,
            "&bTargeting Wand");

    public static void initialize() {
        final SlimefunAddon addon = DEATH_LASERS.getInstance();
        new TargetingWand(
                Groups.DEATH_LASER_GROUP,
                TARGETING_WAND,
                RecipeType.NULL,
                new ItemStack[] {})
                .register(addon);

        new DeathLaser(
                Groups.DEATH_LASER_GROUP,
                DEATH_LASER,
                RecipeType.NULL,
                new ItemStack[] {},
                1000,
                1000)
                .register(addon);
    }
}
