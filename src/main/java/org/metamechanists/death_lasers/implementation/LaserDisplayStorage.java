package org.metamechanists.death_lasers.implementation;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class LaserDisplayStorage {
    private static final Map<Location, LaserBeam> storage = new HashMap<>();

    public static void add(Location location) {
        storage.put(location, new LaserBeam(location));
    }

    public static void remove(Location location) {
        storage.get(location).remove();
        storage.remove(location);
    }

    public static void update() {
        storage.values().forEach(LaserBeam::update);
    }
}
