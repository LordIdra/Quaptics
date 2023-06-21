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

    private static ConnectionGroup getGroupFromGroupLocation(Location groupLocation) {
        return groups.get(groupLocation);
    }

    public static void tick() {
        groups.values().forEach(ConnectionGroup::tick);
    }

    public static void addConnectionPointGroup(Location groupLocation, ConnectionGroup group) {
        groups.put(groupLocation, group);
        group.getPointLocations().forEach(pointLocation -> groupIdFromPointLocation.put(pointLocation, groupLocation));
    }

    public static void removeConnectionPointGroup(Location groupLocation) {
        final ConnectionGroup group = groups.remove(groupLocation);
        group.removeAllPoints();
        group.getPointLocations().forEach(groupIdFromPointLocation::remove);
    }

    public static void updatePointLocation(Location oldLocation, Location newLocation) {
        final Location groupLocation = groupIdFromPointLocation.get(oldLocation);
        final ConnectionGroup group = groups.get(groupLocation);
        groupIdFromPointLocation.remove(oldLocation);
        groupIdFromPointLocation.put(newLocation, groupLocation);
        group.changeLocation(oldLocation, newLocation);
    }

    public static void killAllBeams() {
        groups.values().forEach(ConnectionGroup::killAllBeams);
    }

    public static boolean hasConnectionPoint(Location pointLocation) {
        return groupIdFromPointLocation.containsKey(pointLocation);
    }

    public static Location getGroup(Location pointLocation) {
        return groupIdFromPointLocation.get(pointLocation);
    }

    public static ConnectionGroup getGroup(Block block) {
        final Location location = block.getLocation();
        return ConnectionPointStorage.getGroupFromGroupLocation(new Location(location.getWorld(), location.x(), location.y(), location.z()));
    }

    public static ConnectionPoint getPoint(Location pointLocation) {
        final Location groupLocation = groupIdFromPointLocation.get(pointLocation);
        final ConnectionGroup group = getGroupFromGroupLocation(groupLocation);
        if (group == null) {
            return null;
        }
        return group.getPoint(pointLocation);
    }
}
