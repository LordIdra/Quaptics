package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.panel.PanelAttribute;

import java.util.UUID;

@SuppressWarnings("unused")
public class PanelAttributeId extends CustomId {
    public PanelAttributeId() {
        super();
    }
    public PanelAttributeId(final CustomId id) {
        super(id);
    }
    public PanelAttributeId(final String uuid) {
        super(uuid);
    }
    public PanelAttributeId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable PanelAttribute get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof TextDisplay)
                ? new PanelAttribute(this)
                : null;
    }
}
