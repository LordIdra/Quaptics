package org.metamechanists.death_lasers.connections;

import org.bukkit.Location;

import java.util.Map;
import java.util.Set;

public class ConnectionPointGroup {
    private final Map<Location, ConnectionPoint> points;

    public ConnectionPointGroup(Map<Location, ConnectionPoint> points) {
        this.points = points;
    }

    public void removeAllConnectionPoints() {
        for (ConnectionPoint point : points.values()) {
            point.remove();
        }
    }

    public ConnectionPoint getConnectionPoint(Location location) {
        return points.get(location);
    }

    public Set<Location> getConnectionPointLocations() {
        return points.keySet();
    }
}
