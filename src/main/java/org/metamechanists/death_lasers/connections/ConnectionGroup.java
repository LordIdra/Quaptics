package org.metamechanists.death_lasers.connections;

import org.bukkit.Location;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import java.util.Map;
import java.util.Set;

public class ConnectionGroup {
    private final Map<Location, ConnectionPoint> points;

    public ConnectionGroup(Map<Location, ConnectionPoint> points) {
        this.points = points;
    }

    public void removeAllPoints() {
        for (ConnectionPoint point : points.values()) {
            point.remove();
        }
    }

    public ConnectionPoint getPoint(Location location) {
        return points.get(location);
    }

    public Set<Location> getPointLocations() {
        return points.keySet();
    }
}
