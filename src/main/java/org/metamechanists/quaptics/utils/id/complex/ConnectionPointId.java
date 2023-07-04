package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class ConnectionPointId extends ComplexCustomId {
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
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof Interaction;
    }
    @Override
    public Optional<ConnectionPoint> get() {
        return isValid()
                ? QuapticCache.getConnectionPoint(this)
                : Optional.empty();
    }
}
