package org.metamechanists.death_lasers.storage.connections;

import org.bukkit.Location;
import org.metamechanists.death_lasers.implementation.ConnectionPoint;

import java.util.Map;
import java.util.Set;

public class ConnectionPointGroup {
    private final Map<Location, ConnectionPoint> points;

    public ConnectionPointGroup(Map<Location, ConnectionPoint> points) {
        this.points = points;
    }

    public ConnectionPoint getConnectionPoint(Location location) {
        return points.get(location);
    }

    public Set<Location> getConnectionPointLocations() {
        return points.keySet();
    }
}
