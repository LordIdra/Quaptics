package org.metamechanists.quaptics.utils.id;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.storage.DataTraverser;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class ConnectionPointId extends CustomId {
    public ConnectionPointId() {
        super();
    }
    public ConnectionPointId(final CustomId id) {
        super(id);
    }
    public ConnectionPointId(final String uuid) {
        super(uuid);
    }
    public ConnectionPointId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public Optional<ConnectionPoint> get() {
        if (!(Bukkit.getEntity(getUUID()) instanceof Interaction)) {
            return Optional.empty();
        }

        final DataTraverser traverser = new DataTraverser(this);
        final JsonObject mainSection = traverser.getData();
        final String type = mainSection.get("type").getAsString();
        return Optional.of(type.equals("output")
                ? new ConnectionPointOutput(this)
                : new ConnectionPointInput(this));
    }
}
