package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.connections.ConnectionGroup;

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
    public @Nullable ConnectionGroup get() {
        return DisplayGroup.fromUUID(getUUID()) != null
                ? new ConnectionGroup(this)
                : null;
    }
}
