package org.metamechanists.quaptics.panels;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.simple.InteractionId;
import org.metamechanists.quaptics.utils.id.complex.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Panel {
    @Getter
    private final PanelId id;
    @Getter
    private boolean hidden = true;
    private final Map<String, ? extends PanelAttributeId> attributes;

    public Panel(final Location location, final Map<String, ? extends PanelAttributeId> attributes) {
        this.id = new PanelId(new InteractionBuilder(location).setHeight(0).setWidth(0).build().getUniqueId());
        this.attributes = attributes;
        saveData();
    }

    public Panel(@NotNull final PanelId panelId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(panelId);
        this.id = panelId;
        this.hidden = traverser.getBoolean("hidden");
        this.attributes = traverser.getPanelAttributeIdMap("attributes");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        traverser.set("hidden", hidden);
        traverser.set("attributes", attributes);
    }

    private Optional<Interaction> getInteraction() {
        return new InteractionId(id).get();
    }

    private Optional<PanelAttribute> getAttribute(final String name) {
        return attributes.get(name).get();
    }

    public void setAttributeHidden(final String name, final boolean attributeHidden) {
        getAttribute(name).ifPresent(attribute -> {
            attribute.setHidden(attributeHidden);
            attribute.updateVisibility(hidden);
        });
    }

    public void changeLocation(final Location location) {
        getInteraction().ifPresent(displayGroup -> displayGroup.teleport(location));
        attributes.values().forEach(attributeId -> attributeId.get().ifPresent(attribute -> attribute.changeLocation(location)));
    }

    public void setText(final String name, final Component text) {
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
        attributes.values().stream()
                .map(PanelAttributeId::get)
                .filter(Objects::nonNull)
                .forEach(attributeOptional -> attributeOptional.ifPresent(attribute -> attribute.updateVisibility(hidden)));
    }

    private void removeAttributes() {
        attributes.values().stream()
                .map(PanelAttributeId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(PanelAttribute::remove);
    }

    public void remove() {
        removeAttributes();
        getInteraction().ifPresent(Interaction::remove);
    }
}
