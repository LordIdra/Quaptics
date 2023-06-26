package org.metamechanists.quaptics.connections;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Items;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionGroup {
    @Getter
    private final ConnectionGroupID ID;
    private final String blockID;
    @Getter
    private final Map<String, ConnectionPointID> points = new HashMap<>();

    public ConnectionGroup(ConnectionGroupID ID, ConnectedBlock block, List<ConnectionPoint> pointsIn) {
        this.ID = ID;
        this.blockID = block.getId();
        pointsIn.forEach(point -> points.put(point.getName(), point.getID()));
        saveData();
    }

    private ConnectionGroup(ConnectionGroupID ID) {
        final DataTraverser traverser = new DataTraverser(ID);
        final JsonObject mainSection = traverser.getData();
        final JsonObject pointSection = mainSection.get("points").getAsJsonObject();
        this.ID = ID;
        this.blockID = mainSection.get("blockID").getAsString();
        pointSection.asMap().forEach(
                (key, value) -> points.put(key, new ConnectionPointID(value.getAsString())));
    }

    public static ConnectionGroup fromID(ConnectionGroupID ID) {
        if (Bukkit.getEntity(ID.get()) == null) { return null; }
        return new ConnectionGroup(ID);
    }

    private void saveData() {
        final DataTraverser traverser = new DataTraverser(ID);
        final JsonObject mainSection = traverser.getData();
        final JsonObject pointSection = new JsonObject();
        mainSection.add("blockID", new JsonPrimitive(blockID));
        points.forEach(
                (key, value) -> pointSection.add(key, new JsonPrimitive(value.get().toString())));
        mainSection.add("points", pointSection);
        traverser.save();
    }

    private ConnectionPoint getPoint(ConnectionPointID key) {
        return ConnectionPoint.fromID(key);
    }
    public ConnectionPoint getPoint(String name) {
        return ConnectionPoint.fromID(points.get(name));
    }
    public ConnectionPointOutput getOutput(String name) {
        return (ConnectionPointOutput) getPoint(name);
    }
    public ConnectionPointInput getInput(String name) {
        return (ConnectionPointInput) getPoint(name);
    }
    public ConnectedBlock getBlock() {
        return Items.getBlocks().get(blockID);
    }
    public Location getLocation() {
        return Bukkit.getEntity(getID().get()).getLocation();
    }

    public void tick() {
        getBlock().onQuapticTick(this);
    }

    public void updatePanels() {
        points.values().forEach(ID -> getPoint(ID).updatePanel());
    }

    public void remove() {
        points.values().forEach(ID -> getPoint(ID).remove());
    }

    public void changePointLocation(ConnectionPointID pointId, Location newLocation) {
        getPoint(pointId).changeLocation(newLocation);
    }
}
