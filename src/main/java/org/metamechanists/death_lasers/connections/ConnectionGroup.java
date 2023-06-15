package org.metamechanists.death_lasers.connections;

import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.utils.ConnectionPointLocation;

import java.util.Map;
import java.util.Set;

public class ConnectionGroup {
    private final Map<ConnectionPointLocation, ConnectionPoint> points;

    public ConnectionGroup(Map<ConnectionPointLocation, ConnectionPoint> points) {
        this.points = points;
    }

    public void removeAllPoints() {
        for (ConnectionPoint point : points.values()) {
            point.remove();
        }
    }

    public ConnectionPoint getPoint(ConnectionPointLocation location) {
        return points.get(location);
    }

    public Set<ConnectionPointLocation> getPointLocations() {
        return points.keySet();
    }
}
