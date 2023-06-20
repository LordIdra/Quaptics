package org.metamechanists.death_lasers.items;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import org.bukkit.Material;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.utils.Keys;

public class Groups {
    public static final ItemGroup DEATH_LASER_GROUP = new ItemGroup(Keys.MAIN_GROUP,
            new CustomItemStack(
                    Material.RED_CONCRETE,
                    "&4&lDEATH LASERS"));

    public static void initialize() {
        final SlimefunAddon addon = DEATH_LASERS.getInstance();
        DEATH_LASER_GROUP.register(addon);
    }
}