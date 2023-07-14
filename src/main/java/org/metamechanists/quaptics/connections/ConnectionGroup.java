package org.metamechanists.quaptics.connections;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.panels.info.implementation.PointInfoPanel;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Items;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.storage.QuapticStorage;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConnectionGroup {
    @Getter
    private final ConnectionGroupId id;
    private final String blockId;
    @Getter
    private final Map<String, ConnectionPointId> points;

    public ConnectionGroup(final ConnectionGroupId id, @NotNull final ConnectedBlock block, @NotNull final Iterable<ConnectionPoint> pointsIn) {
        this.id = id;
        this.blockId = block.getId();
        this.points = new HashMap<>();
        pointsIn.forEach(point -> points.put(point.getName(), point.getId()));
        saveData();
    }
    public ConnectionGroup(final ConnectionGroupId id) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        this.id = id;
        this.blockId = traverser.getString("blockId");
        this.points = traverser.getPointIdMap("points");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        traverser.set("blockId", blockId);
        traverser.set("points", points);
    }

    public Optional<ConnectionPoint> getPoint(final String name) {
        return points.get(name).get();
    }
    public ConnectedBlock getBlock() {
        return Items.getBlocks().get(blockId);
    }
    public Optional<Location> getLocation() {
        // The ConnectionGroupId shares the UUID of the main interaction entity
        return Optional.ofNullable(Bukkit.getEntity(id.getUUID())).map(Entity::getLocation);
    }
    public List<ConnectionPoint> getPointList() {
        return points.values().stream()
                .map(ConnectionPointId::get)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
    }
    public List<PointInfoPanel> getPointPanels() {
        return getPointList().stream()
                .map(ConnectionPoint::getPointPanel)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
    }

    public void tick2() {
        final Optional<Location> locationOptional = getLocation();
        locationOptional.ifPresent(location -> getBlock().onTick2(this, location));
    }
    public void tick5() {
        final Optional<Location> locationOptional = getLocation();
        locationOptional.ifPresent(location -> getBlock().onTick5(this, location));
    }
    public void tick21() {
        final Optional<Location> locationOptional = getLocation();
        locationOptional.ifPresent(location -> getBlock().onTick21(this, location));
    }

    public void updatePanels() {
        getPointList().forEach(ConnectionPoint::updatePanel);
    }

    public void remove() {
        getPointList().forEach(ConnectionPoint::remove);
        QuapticStorage.removeGroup(id);
    }
}
