package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.TextDisplay;
import org.metamechanists.quaptics.panel.PanelAttribute;

import java.util.Optional;
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
    public Optional<PanelAttribute> get() {
        return (Bukkit.getEntity(getUUID()) instanceof TextDisplay)
                ? Optional.of(new PanelAttribute(this))
                : Optional.empty();
    }
}
