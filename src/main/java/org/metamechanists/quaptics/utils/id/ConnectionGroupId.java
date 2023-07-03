package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.storage.QuapticCache;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class ConnectionGroupId extends CustomId {
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
    public Optional<ConnectionGroup> get() {
        return Bukkit.getEntity(getUUID()) != null
                ? QuapticCache.getConnectionGroup(this)
                : Optional.empty();
    }
}
