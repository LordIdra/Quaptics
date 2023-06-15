package org.metamechanists.death_lasers.connections;

import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.utils.ConnectionGroupLocation;
import org.metamechanists.death_lasers.utils.ConnectionPointLocation;

import java.util.HashMap;
import java.util.Map;

public class ConnectionPointStorage {
    private static final Map<ConnectionGroupLocation, ConnectionGroup> groups = new HashMap<>();
    private static final Map<ConnectionPointLocation, ConnectionGroupLocation> pointToGroupLocation = new HashMap<>();

    public static void addConnectionPointGroup(ConnectionGroupLocation groupLocation, ConnectionGroup group) {
        groups.put(groupLocation, group);
        for (ConnectionPointLocation pointLocation : group.getPointLocations()) {
            pointToGroupLocation.put(pointLocation, groupLocation);
        }
    }

    public static void removeConnectionPointGroup(ConnectionGroupLocation groupLocation) {
        final ConnectionGroup group = groups.remove(groupLocation);
        for (ConnectionPointLocation pointLocation : group.getPointLocations()) {
            group.removeAllPoints();
            pointToGroupLocation.remove(pointLocation);
        }
    }

    public static void removeAllConnectionPoints() {
        for (ConnectionGroup group : groups.values()) {
            group.removeAllPoints();
        }
    }

    public static ConnectionGroupLocation getGroupLocation(ConnectionPointLocation pointLocation) {
        return pointToGroupLocation.get(pointLocation);
    }

    public static ConnectionGroup getGroup(ConnectionGroupLocation groupLocation) {
        return groups.get(groupLocation);
    }

    public static ConnectionGroup getGroup(ConnectionPointLocation pointLocation) {
        final ConnectionGroupLocation groupLocation = pointToGroupLocation.get(pointLocation);
        return getGroup(groupLocation);
    }

    public static ConnectionPoint getPoint(ConnectionPointLocation pointLocation) {
        return getGroup(pointLocation).getPoint(pointLocation);
    }
}
