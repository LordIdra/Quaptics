package org.metamechanists.death_lasers.lasers.storage;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeamStorage {
    private static final Map<Location, BeamGroup> beamGroups = new HashMap<>();
    private static final List<BeamGroup> deprecatedBeamGroups = new ArrayList<>();

    public static void tick() {
        beamGroups.values().forEach(BeamGroup::tick);
        deprecatedBeamGroups.forEach(BeamGroup::tick);

        // Remove deprecated groups that are ready to remove
        deprecatedBeamGroups.stream()
                .filter(BeamGroup::readyToRemove)
                .forEach(deprecatedBeamGroups::remove);
    }

    public static boolean hasBeamGroup(Location location) {
        return beamGroups.containsKey(location);
    }
    public static void setBeamGroup(Location location, BeamGroup beamGroup) {
        beamGroups.put(location, beamGroup);
    }
    public static void setBeamGroupPowered(Location location,boolean powered) {
        beamGroups.get(location).setPowered(powered);
    }
    public static void deprecateBeamGroup(Location location) {
        beamGroups.get(location).setPowered(false);
        deprecatedBeamGroups.add(beamGroups.remove(location));
    }

    public static void setBeamPowered(Location location, String key, boolean powered) {
        beamGroups.get(location).setPowered(key, powered);
    }
    public static void deprecatedBeam(Location location, String key) {
        beamGroups.get(location).deprecate(key);
    }

    public static void hardRemoveAllBeamGroups() {
        beamGroups.values().forEach(BeamGroup::hardRemoveAllBeams);
        deprecatedBeamGroups.forEach(BeamGroup::hardRemoveAllBeams);
    }
}
