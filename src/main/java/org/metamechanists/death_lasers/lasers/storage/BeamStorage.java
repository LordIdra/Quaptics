package org.metamechanists.death_lasers.lasers.storage;

import org.bukkit.Location;
import org.metamechanists.death_lasers.lasers.beam.Beam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeamStorage {
    private static final Map<Location, List<Beam>> storage = new HashMap<>();
    private static final List<Location> locationsToRemove = new ArrayList<>();

    public static void add(Location location, Beam beam) {
        storage.computeIfAbsent(location, k -> new ArrayList<>());
        storage.get(location).add(beam);
        locationsToRemove.remove(location);
    }

    public static void scheduleLocationRemoval(Location location) {
        locationsToRemove.add(location);
    }

    public static void hardRemoveAllBeams() {
        storage.values().stream().flatMap(Collection::stream).forEach(Beam::remove);
    }

    public static void tick() {
        // Remove expired Beams
        locationsToRemove.forEach(x -> storage.get(x).stream()
                .filter(Beam::readyToRemove)
                .forEach(y -> {
                    y.remove();
                    storage.get(x).remove(y);
                }));

        // Remove empty List<Beam>
        locationsToRemove.stream()
                .filter(location -> storage.get(location).isEmpty())
                .forEach(location -> {
                    storage.remove(location);
                    locationsToRemove.remove(location);
                });

        // Tick remaining beams
        storage.values().forEach(beamList -> beamList.forEach(Beam::tick));
    }
}
