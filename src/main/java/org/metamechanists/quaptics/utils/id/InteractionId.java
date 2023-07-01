package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class InteractionId extends CustomId {
    public InteractionId() {
        super();
    }
    public InteractionId(CustomId id) {
        super(id);
    }
    public InteractionId(String string) {
        super(string);
    }
    public InteractionId(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable Interaction get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof Interaction interaction)
                ? interaction
                : null;
    }
}
