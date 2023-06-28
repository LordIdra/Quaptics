package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class BlockDisplayID extends CustomID {
    public BlockDisplayID() {
        super();
    }
    public BlockDisplayID(CustomID ID) {
        super(ID);
    }
    public BlockDisplayID(String string) {
        super(string);
    }
    public BlockDisplayID(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable BlockDisplay get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof BlockDisplay)
                ? (BlockDisplay) entity
                : null;
    }
}
