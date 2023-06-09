package org.metamechanists.death_lasers.implementation;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class LaserBeam {
    private final List<LaserDisplay> displays = new ArrayList<>();


    public LaserBeam(Location location) {
        displays.add(new LaserDisplay(location));
        update();
    }

    public void remove() {
        displays.forEach(LaserDisplay::remove);
    }

    public void update() {
        displays.removeIf(LaserDisplay::expired);
        displays.forEach(LaserDisplay::update);
    }
}
