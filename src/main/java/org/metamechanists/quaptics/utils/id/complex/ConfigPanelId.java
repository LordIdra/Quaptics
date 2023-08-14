package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.metamechanists.quaptics.panels.config.ConfigPanelContainer;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class ConfigPanelId extends ComplexCustomId {
    public ConfigPanelId() {
        super();
    }
    public ConfigPanelId(final CustomId id) {
        super(id);
    }
    public ConfigPanelId(final String uuid) {
        super(uuid);
    }
    public ConfigPanelId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof Interaction;
    }
    @Override
    public Optional<ConfigPanelContainer> get() {
        return isValid()
                ? QuapticCache.getConfigPanel(this)
                : Optional.empty();
    }
}
