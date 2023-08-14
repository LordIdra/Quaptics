package org.metamechanists.aircraft.items;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.metamechanists.aircraft.items.groups.Aircraft;
import org.metamechanists.aircraft.utils.Keys;


@UtilityClass
public class Groups {
    public final ItemGroup AIRCRAFT = new ItemGroup(Keys.AIRCRAFT,
            new CustomItemStack(Material.TNT, "&aAircraft"));

    public void initialize() {
        Aircraft.initialize();
    }
}
