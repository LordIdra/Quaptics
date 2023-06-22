package org.metamechanists.death_lasers.connections;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.implementation.abstracts.ConnectedBlock;
import org.metamechanists.death_lasers.items.Items;
import org.metamechanists.death_lasers.storage.v1.SerializationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConnectionGroup implements ConfigurationSerializable {
    @Getter
    private final Location location;
    @Getter
    private final ConnectedBlock block;
    private final Map<Location, ConnectionPoint> points;
    private final Map<String, Location> pointLocations;

    public ConnectionGroup(Location location, ConnectedBlock block, Map<String, ConnectionPoint> inputPoints) {
        this.location = location;
        this.block = block;
        this.points = new HashMap<>();
        this.pointLocations = new HashMap<>();
        inputPoints.forEach((name, point) -> {
            points.put(point.getLocation(), point);
            pointLocations.put(name, point.getLocation());
        });
    }

    private ConnectionGroup(Location location, ConnectedBlock block, Map<Location, ConnectionPoint> points, Map<String, Location> pointLocations) {
        this.location = location;
        this.block = block;
        this.points = points;
        this.pointLocations = pointLocations;
    }

    public void updateInfoDisplays() {
        points.values().forEach(ConnectionPoint::updateInfoDisplay);
    }

    public void tick() {
        points.values().forEach(ConnectionPoint::tick);
    }

    public void removeAllPoints() {
        points.values().forEach(ConnectionPoint::remove);
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

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("block",  block.getId());
        map.put("points", SerializationUtils.serializeMap(points, "Location", "ConnectionPoint"));
        map.put("pointLocations", SerializationUtils.serializeMap(pointLocations, "String", "Location"));
        return map;
    }

    public static ConnectionGroup deserialize(Map<String, Object> map) {
        return new ConnectionGroup(
                (Location) map.get("location"),
                Items.getBlocks().get((String) map.get("block")),
                SerializationUtils.deserializeMap((Map<String, Object>) map.get("points"), "Location", "ConnectionPoint"),
                SerializationUtils.deserializeMap((Map<String, Object>) map.get("pointLocations"), "String", "Location"));
    }
}
