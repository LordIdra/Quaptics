package org.metamechanists.death_lasers.implementation;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.entity.display.builders.ItemDisplayBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class LaserDisplay {
    private final DisplayGroup group;
    private double stage = 0;

    public LaserDisplay(Location location) {
        group = new DisplayGroup(location);
        update();
    }

    public void remove() {
        group.removeDisplay("main");
    }

    public void update() {
        group.removeDisplay("main");
        group.addDisplay("MAIN",
                new ItemDisplayBuilder()
                        .setGroupParentOffset(new Vector(0, 1, stage))
                        .setItemStack(new ItemStack(Material.STICK))
                        .build(group));
        stage += 0.2;
    }
}
