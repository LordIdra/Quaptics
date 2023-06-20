package org.metamechanists.death_lasers.connections;

import lombok.Getter;
import org.bukkit.Location;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.abstracts.ConnectedBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConnectionGroup {
    @Getter
    private final ConnectedBlock block;
    private final Map<Location, ConnectionPoint> points = new HashMap<>();
    private final Map<String, Location> pointLocations = new HashMap<>();

    public ConnectionGroup(ConnectedBlock block, Map<String, ConnectionPoint> inputPoints) {
        this.block = block;
        inputPoints.forEach((name, point) -> {
            points.put(point.getLocation(), point);
            pointLocations.put(name, point.getLocation());
        });
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

    public void killAllBeams() {
        for (ConnectionPoint point : points.values()) {
            if (point instanceof ConnectionPointOutput output) {
                output.getLink().killBeam();
            }
        }
    }

    public ConnectionPoint getPoint(Location location) {
        return points.get(location);
    }

    public ConnectionPoint getPoint(String name) {
        return points.get(pointLocations.get(name));
    }

    public Set<Location> getPointLocations() {
        return points.keySet();
    }

    public void changeLocation(Location oldLocation, Location newLocation) {
        final ConnectionPoint point = points.get(oldLocation);
        points.remove(oldLocation);
        points.put(newLocation, point);
        pointLocations.replace(point.getName(), newLocation);
        point.updateLocation(newLocation);
    }
}
