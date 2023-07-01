package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.connections.ConnectionGroup;

import java.util.UUID;

@SuppressWarnings("unused")
public class ConnectionGroupId extends CustomId {
    public ConnectionGroupId() {
        super();
    }
    public ConnectionGroupId(CustomId id) {
        super(id);
    }
    public ConnectionGroupId(String string) {
        super(string);
    }
    public ConnectionGroupId(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable ConnectionGroup get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return DisplayGroup.fromUUID(getUUID()) != null
                ? new ConnectionGroup(this)
                : null;
    }
}
