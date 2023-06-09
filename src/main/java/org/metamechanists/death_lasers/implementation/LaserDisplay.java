package org.metamechanists.death_lasers.implementation;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import org.bukkit.Location;
import org.bukkit.Material;

public class LaserDisplay {
    private static final String MAIN_DISPLAY_NAME = "MAIN";
    private final DisplayGroup group;
    private float stage = 0;

    public LaserDisplay(Location location) {
        group = new DisplayGroup(location);
        group.addDisplay(MAIN_DISPLAY_NAME, new BlockDisplayBuilder()
                .setBlockData(Material.RED_CONCRETE.createBlockData())
                .setTransformation(new TransformationBuilder()
                        .scale(0.2F, 0.2F, 0.8F)
                        .build())
                .build(group));
        update();
    }

    public void remove() {
        group.remove();
    }

    public void update() {
        var display = group.removeDisplay(MAIN_DISPLAY_NAME);
        display.setTransformation(new TransformationBuilder()
                .scale(0.2F, 0.2F, 0.8F)
                .translation(0, 1, stage)
                .build());
        group.addDisplay(MAIN_DISPLAY_NAME, display);
        stage += 0.1;
    }
}
