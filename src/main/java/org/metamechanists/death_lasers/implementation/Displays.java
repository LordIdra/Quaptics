package org.metamechanists.death_lasers.implementation;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.entity.display.builders.ItemDisplayBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Displays {
    private static final Map<Location, DisplayGroup> laserDisplays = new HashMap<>();

    public static void newLaserDisplay(Location location) {
        final DisplayGroup group = new DisplayGroup(location);
        group.addDisplay("MAIN",
                new ItemDisplayBuilder()
                        .setGroupParentOffset(new Vector(0, 1, -0.43))
                        .setItemStack(new ItemStack(Material.STICK))
                        .build(group));
        laserDisplays.put(location, group);
    }

    public static void removeLaserDisplay(Location location) {
        laserDisplays.remove(location);
    }

    public static void updateLaserDisplay(Location location) {
        for (var display : laserDisplays.get(location).getDisplays().values()) {
            display.setDisplayWidth(display.getDisplayWidth() + 1);
        }
    }
}
