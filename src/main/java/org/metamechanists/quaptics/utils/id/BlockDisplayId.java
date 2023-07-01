package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class BlockDisplayId extends CustomId {
    public BlockDisplayId() {
        super();
    }
    public BlockDisplayId(final CustomId id) {
        super(id);
    }
    public BlockDisplayId(final String uuid) {
        super(uuid);
    }
    public BlockDisplayId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable BlockDisplay get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof final BlockDisplay blockDisplay)
                ? blockDisplay
                : null;
    }
}
