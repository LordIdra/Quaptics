package org.metamechanists.quaptics.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;

public class ConnectionPointInput extends ConnectionPoint {
    public ConnectionPointInput(final ConnectionGroupId groupId, final String name, final Location location) {
        super(groupId, name, location, Material.RED_CONCRETE);
    }

    public ConnectionPointInput(final ConnectionPointId pointId) {
        super(pointId);
    }

    @Override
    protected void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(getId());
        traverser.set("type", "input");
        saveData(traverser);
    }
}
