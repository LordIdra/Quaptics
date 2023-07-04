package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class ConnectionGroupId extends ComplexCustomId {
    public ConnectionGroupId() {
        super();
    }
    public ConnectionGroupId(final CustomId id) {
        super(id);
    }
    public ConnectionGroupId(final String uuid) {
        super(uuid);
    }
    public ConnectionGroupId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof Interaction;
    }
    @Override
    public Optional<ConnectionGroup> get() {
        return isValid()
                ? QuapticCache.getConnectionGroup(this)
                : Optional.empty();
    }
}
