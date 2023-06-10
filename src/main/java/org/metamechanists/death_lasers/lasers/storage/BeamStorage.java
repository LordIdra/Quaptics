package org.metamechanists.death_lasers.lasers.storage;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeamStorage {
    private static final Map<Location, BeamGroup> storage = new HashMap<>();
    private static final List<Location> locationsToRemove = new ArrayList<>();

    public static void tick() {
        // Remove expired beam groups
        locationsToRemove.stream()
                .filter(location -> storage.get(location).readyToRemove())
                .forEach(location -> {
                    storage.remove(location);
                    locationsToRemove.remove(location);
                });

        // Tick remaining beams
        storage.values().forEach(BeamGroup::tick);
    }

    public static boolean hasBeamGroup(Location location) {
        return storage.containsKey(location);
    }
    public static void setBeamGroup(Location location, BeamGroup beamGroup) {
        storage.put(location, beamGroup);
        locationsToRemove.remove(location);
    }
    public static void removeBeamGroup(Location location) {
        locationsToRemove.add(location);
    }
    public static void setBeamGroupPowered(Location location,boolean powered) {
        storage.get(location).setPowered(powered);
    }

    public static void removeBeam(Location location, String key) {
        storage.get(location).remove(key);
    }
    public static void setBeamPowered(Location location, String key, boolean powered) {
        storage.get(location).setPowered(key, powered);
    }

    public static void hardRemoveAllBeamGroups() {
        storage.values().forEach(BeamGroup::hardRemoveAllBeams);
    }
}
