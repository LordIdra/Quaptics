package org.metamechanists.death_lasers.implementation;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class LaserBeam {
    private final List<LaserDisplay> displays = new ArrayList<>();
    private final Location location;
    private final int spawnInterval = 10;
    private int timeSinceLastSpawn = 0;


    public LaserBeam(Location location) {
        this.location = location;
    }

    public void remove() {
        displays.forEach(LaserDisplay::remove);
    }

    public void update() {
        timeSinceLastSpawn++;
        if (timeSinceLastSpawn >= spawnInterval) {
            timeSinceLastSpawn = 0;
            displays.add(new LaserDisplay(location));
        }
        displays.stream().filter(LaserDisplay::expired).forEach(LaserDisplay::remove);
        displays.removeIf(LaserDisplay::expired);
        displays.forEach(LaserDisplay::update);
    }
}
