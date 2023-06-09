package org.metamechanists.death_lasers.implementation;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class LaserDisplayStorage {
    private static final Map<Location, LaserDisplay> storage = new HashMap<>();

    public static void newLaserDisplay(Location location) {
        storage.put(location, new LaserDisplay(location));
    }

    public static void removeLaserDisplay(Location location) {
        storage.get(location).remove();
        storage.remove(location);
    }

    public static void updateLaserDisplay(Location location) {
        storage.get(location).update();
    }
}
