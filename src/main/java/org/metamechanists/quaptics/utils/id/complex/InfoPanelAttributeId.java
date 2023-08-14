package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.TextDisplay;
import org.metamechanists.quaptics.panels.info.InfoPanelAttribute;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class InfoPanelAttributeId extends ComplexCustomId {
    public InfoPanelAttributeId() {
        super();
    }
    public InfoPanelAttributeId(final CustomId id) {
        super(id);
    }
    public InfoPanelAttributeId(final String uuid) {
        super(uuid);
    }
    public InfoPanelAttributeId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof TextDisplay;
    }
    @Override
    public Optional<InfoPanelAttribute> get() {
        return isValid()
                ? QuapticCache.getInfoPanelAttribute(this)
                : Optional.empty();
    }
}
