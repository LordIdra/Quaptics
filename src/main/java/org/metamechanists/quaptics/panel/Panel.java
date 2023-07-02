package org.metamechanists.quaptics.panel;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.id.DisplayGroupId;
import org.metamechanists.quaptics.utils.id.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Panel {

    @Getter
    private final DisplayGroupId displayGroupId;
    @Getter
    private boolean hidden = true;
    private final Map<String, PanelAttributeId> attributes;

    public Panel(final DisplayGroupId displayGroupId, final Map<String, PanelAttributeId> attributes) {
        this.displayGroupId = displayGroupId;
        this.attributes = attributes;
        saveData();
    }

    public Panel(@NotNull final PanelId panelId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(panelId);
        this.displayGroupId = new DisplayGroupId(panelId);
        this.hidden = traverser.getBoolean("hidden");
        this.attributes = traverser.getPanelAttributeIdMap("attributes");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroupId);
        traverser.set("hidden", hidden);
        traverser.set("attributes", attributes);
    }

    public PanelId getId() {
        return new PanelId(displayGroupId);
    }

    private Optional<DisplayGroup> getDisplayGroup() {
        return displayGroupId.get();
    }

    @Contract("_ -> new")
    private Optional<PanelAttribute> getAttribute(final String name) {
        return attributes.get(name).get();
    }

    public void setAttributeHidden(final String name, final boolean attributeHidden) {
        getAttribute(name).ifPresent(attribute -> {
            attribute.setHidden(attributeHidden);
            attribute.updateVisibility(hidden);
        });
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
        getDisplayGroup().ifPresent(DisplayGroup::remove);
    }
}
