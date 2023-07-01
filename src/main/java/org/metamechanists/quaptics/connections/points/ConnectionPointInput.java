package org.metamechanists.quaptics.connections.points;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;

public class ConnectionPointInput extends ConnectionPoint {
    public ConnectionPointInput(ConnectionGroupId groupId, String name, Location location) {
        super(groupId, name, location, Material.RED_CONCRETE, 15, 3);
    }

    public ConnectionPointInput(ConnectionPointId id) {
        super(id);
    }

    protected void saveData() {
        final DataTraverser traverser = new DataTraverser(getId());
        final JsonObject mainSection = traverser.getData();
        super.saveData(mainSection);
        mainSection.add("type", new JsonPrimitive("input"));
        traverser.save();
    }
}
