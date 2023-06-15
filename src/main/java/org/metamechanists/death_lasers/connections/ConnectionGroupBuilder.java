package org.metamechanists.death_lasers.connections;

import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.utils.ConnectionPointLocation;

import java.util.HashMap;
import java.util.Map;

public class ConnectionGroupBuilder {
    private final Map<ConnectionPointLocation, ConnectionPoint> points = new HashMap<>();

    public ConnectionGroupBuilder() {}

    public ConnectionGroupBuilder addConnectionPoint(ConnectionPoint point) {
        points.put(point.getLocation(), point);
        return this;
    }

    public ConnectionGroup build() {
        return new ConnectionGroup(points);
    }
}
