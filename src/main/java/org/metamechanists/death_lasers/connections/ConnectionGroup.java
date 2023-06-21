package org.metamechanists.death_lasers.connections;

import lombok.Getter;
import org.bukkit.Location;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.abstracts.ConnectedBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConnectionGroup {
    @Getter
    private final Location location;
    @Getter
    private final ConnectedBlock block;
    private final Map<Location, ConnectionPoint> points = new HashMap<>();
    private final Map<String, Location> pointLocations = new HashMap<>();

    public ConnectionGroup(Location location, ConnectedBlock block, Map<String, ConnectionPoint> inputPoints) {
        this.location = location;
        this.block = block;
        inputPoints.forEach((name, point) -> {
            point.setGroup(this);
            points.put(point.getLocation(), point);
            pointLocations.put(name, point.getLocation());
        });
    }

    public void update() {
        points.values().forEach(ConnectionPoint::update);
    }

    public void tick() {
        points.values().forEach(ConnectionPoint::tick);
    }

    public void removeAllPoints() {
        points.values().stream().filter(point -> point instanceof ConnectionPointInput).forEach(ConnectionPoint::update);
        points.values().stream().filter(point -> point instanceof ConnectionPointOutput).forEach(ConnectionPoint::update);
        points.values().forEach(ConnectionPoint::remove);
    }

    public void killAllBeams() {
        points.values().stream()
                .filter(point -> point instanceof ConnectionPointOutput)
                .map(point -> (ConnectionPointOutput) point)
                .forEach(output -> output.getLink().killBeam());
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
