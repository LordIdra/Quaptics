package org.metamechanists.death_lasers.implementation;

import lombok.val;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class LaserDisplayStorage {
    private static final Map<Location, LaserDisplay> storage = new HashMap<>();

    public static void add(Location location) {
        storage.put(location, new LaserDisplay(location));
    }

    public static void remove(Location location) {
        storage.get(location).remove();
        storage.remove(location);
    }

    public static void update() {
        storage.values().forEach(LaserDisplay::update);
    }
}
