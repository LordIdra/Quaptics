package org.metamechanists.death_lasers.connections;

import org.bukkit.Location;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import java.util.Map;
import java.util.Set;

public class ConnectionGroup {
    private final Map<Location, ConnectionPoint> points;
    private final Map<String, Location> pointNames;

    public ConnectionGroup(Map<Location, ConnectionPoint> points, Map<String, Location> pointNames) {
        this.points = points;
        this.pointNames = pointNames;
    }

    public void tick() {
        for (ConnectionPoint point : points.values()) {
            point.tick();
        }
    }

    public void removeAllPoints() {
        for (ConnectionPoint point : points.values()) {
            point.remove();
        }
    }

    public void killAllPoints() {
        for (ConnectionPoint point : points.values()) {
            point.kill();
        }
    }

    public ConnectionPoint getPoint(Location location) {
        return points.get(location);
    }

    public ConnectionPoint getPoint(String name) {
        return points.get(pointNames.get(name));
    }

    public Set<Location> getPointLocations() {
        return points.keySet();
    }
}
