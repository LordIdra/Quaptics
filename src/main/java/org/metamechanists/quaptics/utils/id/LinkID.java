package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.connections.Link;

import java.util.UUID;

@SuppressWarnings("unused")
public class LinkID extends CustomID {
    public LinkID() {
        super();
    }
    public LinkID(CustomID ID) {
        super(ID);
    }
    public LinkID(String string) {
        super(string);
    }
    public LinkID(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable Link get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return DisplayGroup.fromUUID(getUUID()) != null
                ? new Link(this)
                : null;
    }
}
