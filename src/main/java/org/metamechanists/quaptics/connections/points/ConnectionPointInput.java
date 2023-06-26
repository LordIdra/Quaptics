package org.metamechanists.quaptics.connections.points;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

public class ConnectionPointInput extends ConnectionPoint {
    public ConnectionPointInput(ConnectionGroupID groupID, String name, Location location) {
        super(groupID, name, location, Material.RED_CONCRETE, 15, 3);
    }

    protected ConnectionPointInput(ConnectionPointID ID) {
        super(ID);
    }

    protected void saveData() {
        final DataTraverser traverser = new DataTraverser(getID());
        final JsonObject mainSection = traverser.getData();
        super.saveData(mainSection);
        mainSection.add("type", new JsonPrimitive("input"));
        traverser.save();
    }
}
