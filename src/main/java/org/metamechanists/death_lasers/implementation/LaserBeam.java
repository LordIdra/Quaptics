package org.metamechanists.death_lasers.implementation;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class LaserBeam {
    private final List<LaserDisplay> displays = new ArrayList<>();
    private final Location location;
    private final int spawnInterval = 4;
    private int timeSinceLastSpawn = 0;
    private boolean powered = true;


    public LaserBeam(Location location) {
        this.location = location;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public boolean canBeRemoved() {
        return displays.isEmpty();
    }

    public void remove() {
        displays.forEach(LaserDisplay::remove);
    }

    public void update() {
        if (powered) {
            timeSinceLastSpawn++;
            if (timeSinceLastSpawn >= spawnInterval) {
                timeSinceLastSpawn = 0;
                displays.add(new LaserDisplay(location));
            }
        }

        displays.stream().filter(LaserDisplay::expired).forEach(LaserDisplay::remove);
        displays.removeIf(LaserDisplay::expired);
        displays.forEach(LaserDisplay::update);
    }
}
