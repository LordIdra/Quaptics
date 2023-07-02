package org.metamechanists.quaptics.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
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
        final PersistentDataTraverser persistentDataTraverser = new PersistentDataTraverser(getId());
        persistentDataTraverser.set("type", "output");
        saveData(persistentDataTraverser);
    }
}
