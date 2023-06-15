package org.metamechanists.death_lasers.connections;

import org.bukkit.Location;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import java.util.HashMap;
import java.util.Map;

public class ConnectionGroupBuilder {
    private final Map<Location, ConnectionPoint> points = new HashMap<>();
    private final Map<String, Location> pointNames = new HashMap<>();

    public ConnectionGroupBuilder() {}

    public ConnectionGroupBuilder addConnectionPoint(String name, ConnectionPoint point) {
        pointNames.put(name, point.getLocation());
        points.put(point.getLocation(), point);
        return this;
    }

    public ConnectionGroup build() {
        return new ConnectionGroup(points, pointNames);
    }
}
