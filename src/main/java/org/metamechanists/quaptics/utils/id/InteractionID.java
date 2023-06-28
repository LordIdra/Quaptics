package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class InteractionID extends CustomID {
    public InteractionID() {
        super();
    }
    public InteractionID(CustomID ID) {
        super(ID);
    }
    public InteractionID(String string) {
        super(string);
    }
    public InteractionID(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable Interaction get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof Interaction)
                ? (Interaction) entity
                : null;
    }
}
