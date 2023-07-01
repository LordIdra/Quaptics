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
    public TextDisplayId(CustomId id) {
        super(id);
    }
    public TextDisplayId(String string) {
        super(string);
    }
    public TextDisplayId(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable TextDisplay get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof TextDisplay textDisplay)
                ? textDisplay
                : null;
    }
}
