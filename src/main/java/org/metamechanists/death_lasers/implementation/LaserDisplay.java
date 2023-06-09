package org.metamechanists.death_lasers.implementation;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;

public class LaserDisplay {
    private final int max_age = 40;
    private int age = 0;
    private final BlockDisplay display;

    public LaserDisplay(Location location) {
        display = new BlockDisplayBuilder()
                .setLocation(location)
                .setBlockData(Material.RED_CONCRETE.createBlockData())
                .setGroupParentOffset(new Vector())
                .build();
        update();
    }

    public void remove() {
        display.remove();
    }

    public void update() {
        final float scale = 1 - ((float)age / (float)max_age);
        display.setTransformation(new TransformationBuilder()
                .scale(0.2F*scale, 0.2F*scale, 0.2F*scale)
                .translation(0.4F, 0.4F, age*0.5F)
                .build());
        age++;
    }

    public boolean expired() {
        return age >= max_age;
    }
}
