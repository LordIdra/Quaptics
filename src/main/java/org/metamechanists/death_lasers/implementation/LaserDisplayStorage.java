package org.metamechanists.death_lasers.implementation;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaserDisplayStorage {
    private static final Map<Location, LaserBeam> storage = new HashMap<>();
    private static final List<Location> locationsToRemove = new ArrayList<>();

    public static void add(Location location) {
        storage.put(location, new LaserBeam(location));
    }

    public static void scheduleForRemoval(Location location) {
        storage.get(location).setPowered(false);
        locationsToRemove.add(location);
    }

    public static void hardRemoveAllLasers() {
        storage.values().forEach(LaserBeam::remove);
    }

    public static void update() {
        locationsToRemove.stream()
                .filter(location -> storage.get(location).canBeRemoved())
                .forEach(location -> {
            storage.get(location).remove();
            storage.remove(location);
            locationsToRemove.remove(location);
        });

        storage.values().forEach(LaserBeam::update);
    }
}
