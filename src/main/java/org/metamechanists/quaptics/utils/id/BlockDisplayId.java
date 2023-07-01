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
    public BlockDisplayId(CustomId id) {
        super(id);
    }
    public BlockDisplayId(String string) {
        super(string);
    }
    public BlockDisplayId(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable BlockDisplay get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof BlockDisplay blockDisplay)
                ? blockDisplay
                : null;
    }
}
