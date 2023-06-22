package org.metamechanists.death_lasers.connections;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.implementation.base.ConnectedBlock;
import org.metamechanists.death_lasers.items.Items;
import org.metamechanists.death_lasers.storage.SerializationUtils;
import org.metamechanists.death_lasers.utils.id.ConnectionGroupID;
import org.metamechanists.death_lasers.utils.id.ConnectionPointID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionGroup implements ConfigurationSerializable {
    @Getter
    private final ConnectionGroupID id;
    @Getter
    private final Location location;
    @Getter
    private final ConnectedBlock block;
    @Getter
    private final Map<ConnectionPointID, ConnectionPoint> points;
    private final Map<String, ConnectionPointID> pointUuidsFromNames;

    public ConnectionGroup(Location location, ConnectedBlock block, List<ConnectionPoint> inputPoints) {
        this.id = new ConnectionGroupID();
        this.location = location;
        this.block = block;
        this.points = new HashMap<>();
        this.pointUuidsFromNames = new HashMap<>();
        inputPoints.forEach(point -> {
            points.put(point.getId(), point);
            pointUuidsFromNames.put(point.getName(), point.getId());
        });
    }

    private ConnectionGroup(ConnectionGroupID id, Location location, ConnectedBlock block,
                            Map<ConnectionPointID, ConnectionPoint> points, Map<String, ConnectionPointID> pointUuidsFromNames) {
        this.id = id;
        this.location = location;
        this.block = block;
        this.points = points;
        this.pointUuidsFromNames = pointUuidsFromNames;
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

    public ConnectionPoint getPoint(ConnectionPointID uuid) {
        return points.get(uuid);
    }

    public ConnectionPoint getPoint(String name) {
        return points.get(pointUuidsFromNames.get(name));
    }

    public void changePointLocation(ConnectionPointID pointId, Location newLocation) {
        points.get(pointId).updateLocation(newLocation);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("location", location);
        map.put("block",  block.getId());
        map.put("points", SerializationUtils.serializeMap(points, "UUID", "ConnectionPoint"));
        map.put("pointLocations", SerializationUtils.serializeMap(pointUuidsFromNames, "String", "UUID"));
        return map;
    }

    public static ConnectionGroup deserialize(Map<String, Object> map) {
        return new ConnectionGroup(
                (ConnectionGroupID) map.get("id"),
                (Location) map.get("location"),
                Items.getBlocks().get((String) map.get("block")),
                SerializationUtils.deserializeMap((Map<String, Object>) map.get("points"), "UUID", "ConnectionPoint"),
                SerializationUtils.deserializeMap((Map<String, Object>) map.get("pointLocations"), "String", "UUID"));
    }
}
