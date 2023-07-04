package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.TextDisplay;
import org.metamechanists.quaptics.panels.PanelAttribute;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class PanelAttributeId extends ComplexCustomId {
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
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof TextDisplay;
    }
    @Override
    public Optional<PanelAttribute> get() {
        return isValid()
                ? QuapticCache.getPanelAttribute(this)
                : Optional.empty();
    }
}
