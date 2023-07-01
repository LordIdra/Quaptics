package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;

import java.util.Optional;
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
    public Optional<BlockDisplay> get() {
        return (Bukkit.getEntity(getUUID()) instanceof final BlockDisplay blockDisplay)
                ? Optional.of(blockDisplay)
                : Optional.empty();
    }
}
