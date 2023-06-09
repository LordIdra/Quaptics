package org.metamechanists.death_lasers.implementation;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import org.bukkit.Location;

public class LaserDisplay {
    private static final String MAIN_DISPLAY_NAME = "MAIN";
    private final DisplayGroup group;
    private float stage = 0;

    public LaserDisplay(Location location) {
        group = new DisplayGroup(location);
        update();
    }

    public void remove() {
        group.removeDisplay(MAIN_DISPLAY_NAME);
    }

    public void update() {
        var display = group.removeDisplay(MAIN_DISPLAY_NAME);
        display.setTransformation(new TransformationBuilder().translation(0, 1, stage).build());
        group.addDisplay(MAIN_DISPLAY_NAME, display);
        stage += 0.1;
    }
}
