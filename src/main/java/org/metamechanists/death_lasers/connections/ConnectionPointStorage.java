package org.metamechanists.death_lasers.connections;

import org.bukkit.Location;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import java.util.HashMap;
import java.util.Map;

public class ConnectionPointStorage {
    private static final Map<Location, ConnectionGroup> groups = new HashMap<>();
    private static final Map<Location, Location> groupIdFromPointLocation = new HashMap<>();

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

    public static void removeAllConnectionPoints() {
        for (ConnectionGroup group : groups.values()) {
            group.removeAllPoints();
        }
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
        return getGroupFromPointLocation(pointLocation).getPoint(pointLocation);
    }
}
