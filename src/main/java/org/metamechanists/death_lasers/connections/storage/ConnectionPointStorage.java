package org.metamechanists.death_lasers.connections.storage;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import java.util.HashMap;
import java.util.Map;

public class ConnectionPointStorage {
    @Getter
    @Setter
    private static Map<Location, ConnectionGroup> groups = new HashMap<>();
    @Getter
    @Setter
    private static Map<Location, Location> groupIdsFromPointLocations = new HashMap<>();

    private static ConnectionGroup getGroupFromGroupLocation(Location groupLocation) {
        return groups.get(groupLocation);
    }

    public static void tick() {
        groups.values().forEach(ConnectionGroup::tick);
    }

    public static void addConnectionPointGroup(Location groupLocation, ConnectionGroup group) {
        groups.put(groupLocation, group);
        group.getPointLocations().forEach(pointLocation -> groupIdsFromPointLocations.put(pointLocation, groupLocation));
    }

    public static void removeConnectionPointGroup(Location groupLocation) {
        final ConnectionGroup group = groups.remove(groupLocation);
        group.removeAllPoints();
        group.getPointLocations().forEach(groupIdsFromPointLocations::remove);
    }

    public static void updatePointLocation(Location oldLocation, Location newLocation) {
        final Location groupLocation = groupIdsFromPointLocations.get(oldLocation);
        final ConnectionGroup group = groups.get(groupLocation);
        groupIdsFromPointLocations.remove(oldLocation);
        groupIdsFromPointLocations.put(newLocation, groupLocation);
        group.changeLocation(oldLocation, newLocation);
    }

    public static void killAllBeams() {
        groups.values().forEach(ConnectionGroup::killAllBeams);
    }

    public static boolean hasPoint(Location pointLocation) {
        return groupIdsFromPointLocations.containsKey(pointLocation);
    }

    public static boolean hasGroup(Location groupLocation) {
        return groups.containsKey(groupLocation);
    }

    public static Location getGroupLocation(Location pointLocation) {
        return groupIdsFromPointLocations.get(pointLocation);
    }

    public static ConnectionGroup getGroup(Location pointLocation) {
        return groups.get(getGroupLocation(pointLocation));
    }

    public static ConnectionGroup getGroupLocation(Block block) {
        final Location location = block.getLocation();
        return ConnectionPointStorage.getGroupFromGroupLocation(new Location(location.getWorld(), location.x(), location.y(), location.z()));
    }

    public static ConnectionPoint getPoint(Location pointLocation) {
        final Location groupLocation = groupIdsFromPointLocations.get(pointLocation);
        final ConnectionGroup group = getGroupFromGroupLocation(groupLocation);
        if (group == null) {
            return null;
        }
        return group.getPoint(pointLocation);
    }
}
