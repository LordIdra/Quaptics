package org.metamechanists.quaptics.panels.info;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.quaptics.utils.id.simple.InteractionId;

import java.util.Map;
import java.util.Optional;

public class InfoPanelContainer {
    @Getter
    private final InfoPanelId id;
    @Getter
    private boolean hidden = true;
    private final Vector spacing;
    private final Map<String, ? extends InfoPanelAttributeId> attributes;

    public InfoPanelContainer(final Location location, final Vector spacing, final Map<String, ? extends InfoPanelAttributeId> attributes) {
        this.id = new InfoPanelId(new InteractionBuilder(location).setHeight(0).setWidth(0).build().getUniqueId());
        this.spacing = spacing;
        this.attributes = attributes;
        saveData();
    }

    public InfoPanelContainer(@NotNull final InfoPanelId panelId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(panelId);
        this.id = panelId;
        this.hidden = traverser.getBoolean("hidden");
        this.spacing = traverser.getVector("spacing");
        this.attributes = traverser.getInfoPanelAttributeIdMap("attributes");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        traverser.set("hidden", hidden);
        traverser.set("spacing", spacing);
        traverser.set("attributes", attributes);
    }

    private Optional<Interaction> getInteraction() {
        return new InteractionId(id).get();
    }

    private Optional<InfoPanelAttribute> getAttribute(final String name) {
        return attributes.get(name).get();
    }

    public void setAttributeHidden(final String name, final boolean attributeHidden) {
        getAttribute(name).ifPresent(attribute -> {
            attribute.setHidden(attributeHidden);
            attribute.updateVisibility(hidden);
        });
        reorderAttributes();
    }

    public void changeLocation(final Location location) {
        getInteraction().ifPresent(displayGroup -> displayGroup.teleport(location));
        attributes.values().forEach(attributeId -> attributeId.get().ifPresent(attribute -> attribute.changeLocation(location)));
    }

    public void setText(final String name, final String text) {
        getAttribute(name).ifPresent(attribute -> attribute.setText(text));
    }

    public void setHidden(final boolean hidden) {
        if (this.hidden != hidden) {
            this.hidden = hidden;
            updateAttributeVisibility();
            saveData();
        }
    }

    public void toggleHidden() {
        setHidden(!hidden);
    }

    private void updateAttributeVisibility() {
        reorderAttributes();
        attributes.values().stream()
                .map(InfoPanelAttributeId::get)
                .filter(Optional::isPresent)
                .forEach(attributeOptional -> attributeOptional.ifPresent(attribute -> attribute.updateVisibility(hidden)));
    }

    private void removeAttributes() {
        attributes.values().stream()
                .map(InfoPanelAttributeId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(InfoPanelAttribute::remove);
    }

    private void reorderAttributes() {
        final Optional<Interaction> interaction = getInteraction();
        if (interaction.isEmpty()) {
            return;
        }

        final Location location = interaction.get().getLocation().clone();

        attributes.values().stream()
                .map(InfoPanelAttributeId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(attribute -> {
                    attribute.changeLocation(location);
                    location.subtract(spacing);
                });
    }

    public void remove() {
        removeAttributes();
        getInteraction().ifPresent(Interaction::remove);
    }
}
