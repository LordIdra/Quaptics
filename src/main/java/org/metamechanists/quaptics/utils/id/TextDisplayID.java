package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class TextDisplayID extends CustomID {
    public TextDisplayID() {
        super();
    }
    public TextDisplayID(CustomID ID) {
        super(ID);
    }
    public TextDisplayID(String string) {
        super(string);
    }
    public TextDisplayID(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable TextDisplay get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof TextDisplay)
                ? (TextDisplay) entity
                : null;
    }
}
