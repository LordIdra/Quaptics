package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class TextDisplayId extends CustomId {
    public TextDisplayId() {
        super();
    }
    public TextDisplayId(final CustomId id) {
        super(id);
    }
    public TextDisplayId(final String uuid) {
        super(uuid);
    }
    public TextDisplayId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable TextDisplay get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof final TextDisplay textDisplay)
                ? textDisplay
                : null;
    }
}
