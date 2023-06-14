package org.metamechanists.death_lasers.storage.beams;

import org.bukkit.Location;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BeamStorage {
    private static final Map<Location, BeamGroup> beamGroups = new ConcurrentHashMap<>();
    private static final Queue<BeamGroup> deprecatedBeamGroups = new ConcurrentLinkedQueue<>();

    public static void tick() {
        beamGroups.values().forEach(BeamGroup::tick);
        deprecatedBeamGroups.forEach(BeamGroup::tick);

        // Remove deprecated groups that are ready to remove
        deprecatedBeamGroups.stream()
                .filter(BeamGroup::readyToRemove)
                .forEach(deprecatedBeamGroups::remove);
    }

    // debug methods
    public static Collection<Location> getBeamGroupLocations() {
        return beamGroups.keySet();
    }
    public static int getNumberOfActiveBeamGroups() {
        return beamGroups.size();
    }
    public static int getNumberOfActiveBeams() {
        int i = 0;
        for (var group : beamGroups.values()) {
            i += group.getNumberOfActiveBeams();
        }
        return i;
    }
    public static int getNumberOfDeprecatedBeamGroups() {
        return deprecatedBeamGroups.size();
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
        beamGroups.get(location).deprecateAllBeams();
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
