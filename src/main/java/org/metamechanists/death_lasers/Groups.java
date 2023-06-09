package org.metamechanists.death_lasers;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import org.bukkit.Material;

public class Groups {
    public static final ItemGroup DEATH_LASER_GROUP = new ItemGroup(Keys.MAIN_GROUP,
            new CustomItemStack(
                    Material.RED_CONCRETE,
                    "&4&lDEATH LASERS"));
}
