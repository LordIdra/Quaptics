package org.metamechanists.quaptics.connections;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.Objects;

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

    public @Nullable ConnectionPoint getPoint(final String name) {
        return points.get(name).get();
    }
    public @Nullable ConnectionPointOutput getOutput(final String name) {
        return getPoint(name) instanceof final ConnectionPointOutput output
                ? output
                : null;
    }
    public @Nullable ConnectionPointInput getInput(final String name) {
        return getPoint(name) instanceof final ConnectionPointInput input
                ? input
                : null;
    }
    public ConnectedBlock getBlock() {
        return Items.getBlocks().get(blockId);
    }
    public @Nullable Location getLocation() {
        // The ConnectionGroupId shares the UUID of the main interaction entity
        final Entity entity = Bukkit.getEntity(id.getUUID());
        return entity != null
                ? entity.getLocation()
                : null;
    }

    public void tick() {
        getBlock().onQuapticTick(this);
    }

    public void updatePanels() {
        points.values().stream()
                .map(ConnectionPointId::get)
                .filter(Objects::nonNull)
                .forEach(ConnectionPoint::updatePanel);
    }

    public void remove() {
        points.values().stream()
                .map(ConnectionPointId::get)
                .filter(Objects::nonNull)
                .forEach(ConnectionPoint::remove);
    }
}
