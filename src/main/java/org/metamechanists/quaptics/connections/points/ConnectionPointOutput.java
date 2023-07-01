package org.metamechanists.quaptics.connections.points;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;

public class ConnectionPointOutput extends ConnectionPoint {
    public ConnectionPointOutput(final ConnectionGroupId groupId, final String name, final Location location) {
        super(groupId, name, location, Material.LIME_CONCRETE);
    }

    public ConnectionPointOutput(final ConnectionPointId pointId) {
        super(pointId);
    }

    @Override
    protected void saveData() {
        final DataTraverser traverser = new DataTraverser(getId());
        final JsonObject mainSection = traverser.getData();
        super.saveData(mainSection);
        mainSection.add("type", new JsonPrimitive("output"));
        traverser.save();
    }
}
