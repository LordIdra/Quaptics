package org.metamechanists.quaptics.connections.points;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

public class ConnectionPointOutput extends ConnectionPoint {
    public ConnectionPointOutput(ConnectionGroupID groupID, String name, Location location) {
        super(groupID, name, location, Material.LIME_CONCRETE, 15, 3);
    }

    protected ConnectionPointOutput(ConnectionPointID ID) {
        super(ID);
    }

    @Override
    public void tick() {
        if (hasLink()) {
            getLink().tick();
        }
    }

    protected void saveData() {
        final DataTraverser traverser = new DataTraverser(getID());
        final JsonObject mainSection = traverser.getData();
        super.saveData(mainSection);
        mainSection.add("type", new JsonPrimitive("output"));
        traverser.save();
    }
}
