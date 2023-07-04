package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.metamechanists.quaptics.panels.Panel;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class PanelId extends ComplexCustomId {
    public PanelId() {
        super();
    }
    public PanelId(final CustomId id) {
        super(id);
    }
    public PanelId(final String uuid) {
        super(uuid);
    }
    public PanelId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof Interaction;
    }
    @Override
    public Optional<Panel> get() {
        return isValid()
                ? QuapticCache.getPanel(this)
                : Optional.empty();
    }
}
