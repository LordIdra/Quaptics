package org.metamechanists.quaptics.items;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import org.bukkit.Material;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.utils.Colors;
import org.metamechanists.quaptics.utils.Keys;

public class Groups {
    public static final ItemGroup MAIN_GROUP = new ItemGroup(Keys.MAIN_GROUP,
            new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS, Colors.QUAPTICS.getString() + "Quaptics"));

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();
        MAIN_GROUP.register(addon);
    }
}
