package org.metamechanists.quaptics.connections;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Items;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConnectionGroup {
    @Getter
    private final ConnectionGroupId id;
    private final String blockId;
    @Getter
    private final Map<String, ConnectionPointId> points = new HashMap<>();

    public ConnectionGroup(final ConnectionGroupId id, @NotNull final ConnectedBlock block, @NotNull final Iterable<ConnectionPoint> pointsIn) {
        this.id = id;
        this.blockId = block.getId();
        pointsIn.forEach(point -> points.put(point.getName(), point.getId()));
        saveData();
    }

    public ConnectionGroup(final ConnectionGroupId id) {
        final DataTraverser traverser = new DataTraverser(id);
        final JsonObject mainSection = traverser.getData();
        final JsonObject pointSection = mainSection.get("points").getAsJsonObject();
        this.id = id;
        this.blockId = mainSection.get("blockId").getAsString();
        pointSection.asMap().forEach(
                (key, value) -> points.put(key, new ConnectionPointId(value.getAsString())));
    }

    private void saveData() {
        final DataTraverser traverser = new DataTraverser(id);
        final JsonObject mainSection = traverser.getData();
        final JsonObject pointSection = new JsonObject();
        mainSection.add("blockId", new JsonPrimitive(blockId));
        points.forEach(
                (key, value) -> pointSection.add(key, new JsonPrimitive(value.getUUID().toString())));
        mainSection.add("points", pointSection);
        traverser.save();
    }

    public Optional<ConnectionPoint> getPoint(final String name) {
        return points.get(name).get();
    }
    public Optional<ConnectionPointOutput> getOutput(final String name) {
        return getPoint(name).map(point -> (ConnectionPointOutput) point);
    }
    public Optional<ConnectionPointInput> getInput(final String name) {
        return getPoint(name).map(point -> (ConnectionPointInput) point);
    }
    public ConnectedBlock getBlock() {
        return Items.getBlocks().get(blockId);
    }
    public Optional<Location> getLocation() {
        // The ConnectionGroupId shares the UUID of the main interaction entity
        return Optional.ofNullable(Bukkit.getEntity(id.getUUID())).map(Entity::getLocation);
    }

    public void tick() {
        getBlock().onQuapticTick(this);
    }

    public void updatePanels() {
        points.values().stream()
                .map(ConnectionPointId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(ConnectionPoint::updatePanel);
    }

    public void remove() {
        points.values().stream()
                .map(ConnectionPointId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(ConnectionPoint::remove);
    }
}
