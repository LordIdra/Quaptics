package org.metamechanists.death_lasers.connections;

import org.bukkit.Location;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import java.util.HashMap;
import java.util.Map;

public class ConnectionGroupBuilder {
    private final Map<Location, ConnectionPoint> points = new HashMap<>();

    public ConnectionGroupBuilder() {}

    public ConnectionGroupBuilder addConnectionPoint(ConnectionPoint point) {
        points.put(point.getLocation(), point);
        return this;
    }

    public ConnectionGroup build() {
        return new ConnectionGroup(points);
    }
}
