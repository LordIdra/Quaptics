package org.metamechanists.quaptics.panels.config;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.displaymodellib.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.simple.InteractionId;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ConfigPanelContainer {
    @Getter
    private final ConfigPanelId id;
    @Getter
    private boolean hidden = true;
    private final Map<String, ? extends ConfigPanelAttributeId> attributes;

    public ConfigPanelContainer(final Location location, final Map<String, ? extends ConfigPanelAttributeId> attributes) {
        this.id = new ConfigPanelId(new InteractionBuilder().height(0).width(0).build(location).getUniqueId());
        this.attributes = attributes;
        saveData();
    }

    public ConfigPanelContainer(@NotNull final ConfigPanelId panelId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(panelId);
        this.id = panelId;
        this.hidden = traverser.getBoolean("hidden");
        this.attributes = traverser.getConfigPanelAttributeIdMap("attributes");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        traverser.set("hidden", hidden);
        traverser.set("attributes", attributes);
    }

    private Optional<Interaction> getInteraction() {
        return new InteractionId(id).get();
    }

    private Optional<ConfigPanelAttribute> getAttribute(final String name) {
        return attributes.get(name).get();
    }

    public void setValue(final String name, final String value) {
        getAttribute(name).ifPresent(attribute -> attribute.setValue(value));
    }

    public void setHidden(final boolean hidden) {
        if (this.hidden != hidden) {
            this.hidden = hidden;
            updateAttributeVisibility();
            saveData();
        }
    }

    private void updateAttributeVisibility() {
        attributes.values().stream()
                .map(ConfigPanelAttributeId::get)
                .filter(Objects::nonNull)
                .forEach(attributeOptional -> attributeOptional.ifPresent(attribute -> attribute.setHidden(hidden)));
    }

    private void removeAttributes() {
        attributes.values().stream()
                .map(ConfigPanelAttributeId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(ConfigPanelAttribute::remove);
    }

    public void remove() {
        removeAttributes();
        getInteraction().ifPresent(Interaction::remove);
    }
}
