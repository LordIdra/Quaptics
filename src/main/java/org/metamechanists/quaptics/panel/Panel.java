package org.metamechanists.quaptics.panel;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.DisplayGroupId;
import org.metamechanists.quaptics.utils.id.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Panel {

    @Getter
    private final DisplayGroupId displayGroupId;
    @Getter
    private boolean hidden = true;
    private final Map<String, PanelAttributeId> attributes;

    public Panel(DisplayGroupId displayGroupId, Map<String, PanelAttributeId> attributes) {
        this.displayGroupId = displayGroupId;
        this.attributes = attributes;
        saveData();
    }

    public Panel(@NotNull PanelId panelId) {
        final DataTraverser traverser = new DataTraverser(panelId);
        final JsonObject mainSection = traverser.getData();
        final JsonObject attributeSection = mainSection.get("attributes").getAsJsonObject();
        this.displayGroupId = new DisplayGroupId(panelId);
        this.hidden = mainSection.get("hidden").getAsBoolean();
        this.attributes = new HashMap<>();
        attributeSection.asMap().forEach(
                (key, value) -> attributes.put(key, new PanelAttributeId(value.getAsString())));
    }

    private void saveData() {
        final DataTraverser traverser = new DataTraverser(displayGroupId);
        final JsonObject mainSection = traverser.getData();
        final JsonObject attributeSection = new JsonObject();
        mainSection.add("hidden", new JsonPrimitive(hidden));
        this.attributes.forEach(
                (key, value) -> attributeSection.add(key, new JsonPrimitive(value.getUUID().toString())));
        mainSection.add("attributes", attributeSection);
        traverser.save();
    }

    public PanelId getId() {
        return new PanelId(displayGroupId);
    }

    private DisplayGroup getDisplayGroup() {
        return DisplayGroup.fromUUID(displayGroupId.getUUID());
    }

    @Contract("_ -> new")
    private @Nullable PanelAttribute getAttribute(final String name) {

        return attributes.get(name).get();
    }

    public void setAttributeHidden(final String name, final boolean attributeHidden) {
        getAttribute(name).setHidden(attributeHidden);
        getAttribute(name).updateVisibility(hidden);
    }

    public void setText(final String name, final String text) {
        getAttribute(name).setText(text);
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
                .forEach(attribute -> attribute.updateVisibility(hidden));
    }

    private void removeAttributes() {
        attributes.values().stream()
                .map(PanelAttributeId::get)
                .filter(Objects::nonNull)
                .forEach(PanelAttribute::remove);
    }

    public void remove() {
        removeAttributes();
        final DisplayGroup displayGroup = getDisplayGroup();
        if (displayGroup != null) {
            displayGroup.remove();
        }
    }
}
