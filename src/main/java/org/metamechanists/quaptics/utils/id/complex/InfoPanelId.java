package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class InfoPanelId extends ComplexCustomId {
    public InfoPanelId() {
        super();
    }
    public InfoPanelId(final CustomId id) {
        super(id);
    }
    public InfoPanelId(final String uuid) {
        super(uuid);
    }
    public InfoPanelId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof Interaction;
    }
    @Override
    public Optional<InfoPanelContainer> get() {
        return isValid()
                ? QuapticCache.getInfoPanel(this)
                : Optional.empty();
    }
}
