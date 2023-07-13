package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.metamechanists.quaptics.panels.config.ConfigPanelAttribute;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class ConfigPanelAttributeId extends ComplexCustomId {
    public ConfigPanelAttributeId() {
        super();
    }
    public ConfigPanelAttributeId(final CustomId id) {
        super(id);
    }
    public ConfigPanelAttributeId(final String uuid) {
        super(uuid);
    }
    public ConfigPanelAttributeId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof Interaction;
    }
    @Override
    public Optional<ConfigPanelAttribute> get() {
        return isValid()
                ? QuapticCache.getConfigPanelAttribute(this)
                : Optional.empty();
    }
}
