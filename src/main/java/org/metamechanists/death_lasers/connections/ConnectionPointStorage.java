package org.metamechanists.death_lasers.connections;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConnectionPointStorage implements Serializable {
    private static final Map<Location, ConnectionGroup> groups = new HashMap<>();
    private static final Map<Location, Location> groupIdFromPointLocation = new HashMap<>();

    public static void tick() {
        for (ConnectionGroup group : groups.values()) {
            group.tick();
        }
    }

    public static void addConnectionPointGroup(Location groupLocation, ConnectionGroup group) {
        groups.put(groupLocation, group);
        for (Location pointLocation : group.getPointLocations()) {
            groupIdFromPointLocation.put(pointLocation, groupLocation);
        }
    }

    public static void removeConnectionPointGroup(Location groupLocation) {
        final ConnectionGroup group = groups.remove(groupLocation);
        for (Location pointLocation : group.getPointLocations()) {
            group.removeAllPoints();
            groupIdFromPointLocation.remove(pointLocation);
        }
    }

    public static void killAllBeams() {
        for (ConnectionGroup group : groups.values()) {
            group.killAllBeams();
        }
    }

    public static boolean hasConnectionPoint(Location pointLocation) {
        return groupIdFromPointLocation.containsKey(pointLocation);
    }

    public static Location getGroupLocationFromPointLocation(Location pointLocation) {
        return groupIdFromPointLocation.get(pointLocation);
    }

    public static ConnectionGroup getGroupFromGroupLocation(Location groupLocation) {
        return groups.get(groupLocation);
    }

    public static ConnectionGroup getGroupFromPointLocation(Location pointLocation) {
        final Location groupLocation = groupIdFromPointLocation.get(pointLocation);
        return getGroupFromGroupLocation(groupLocation);
    }

    public static ConnectionPoint getPointFromPointLocation(Location pointLocation) {
        ConnectionGroup group = getGroupFromPointLocation(pointLocation);
        if (group == null) {
            return null;
        }
        return group.getPoint(pointLocation);
    }

    public static ConnectionGroup getGroupFromBlock(Block block) {
        final Location location = block.getLocation();
        return ConnectionPointStorage.getGroupFromGroupLocation(new Location(location.getWorld(), location.x(), location.y(), location.z()));
    }

    public static void updateLocation(Location oldLocation, Location newLocation) {
        final Location groupLocation = groupIdFromPointLocation.get(oldLocation);
        final ConnectionGroup group = groups.get(groupLocation);
        groupIdFromPointLocation.remove(oldLocation);
        groupIdFromPointLocation.put(newLocation, groupLocation);
        group.changeLocation(oldLocation, newLocation);
    }
}
