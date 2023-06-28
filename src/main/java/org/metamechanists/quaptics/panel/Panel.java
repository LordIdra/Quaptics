package org.metamechanists.quaptics.panel;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.DisplayGroupID;
import org.metamechanists.quaptics.utils.id.PanelAttributeID;
import org.metamechanists.quaptics.utils.id.PanelID;

import java.util.HashMap;
import java.util.Map;

public class Panel {

    @Getter
    private final DisplayGroupID displayGroupID;
    @Getter
    private boolean hidden = true;
    private final Map<String, PanelAttributeID> attributes;

    public Panel(DisplayGroupID displayGroupID, Map<String, PanelAttributeID> attributes) {
        this.displayGroupID = displayGroupID;
        this.attributes = attributes;
        saveData();
    }

    private Panel(@NotNull PanelID panelID) {
        final DataTraverser traverser = new DataTraverser(panelID);
        final JsonObject mainSection = traverser.getData();
        final JsonObject attributeSection = mainSection.get("attributes").getAsJsonObject();
        this.displayGroupID = new DisplayGroupID(panelID);
        this.hidden = mainSection.get("hidden").getAsBoolean();
        this.attributes = new HashMap<>();
        attributeSection.asMap().forEach(
                (key, value) -> attributes.put(key, new PanelAttributeID(value.getAsString())));
    }

    @Contract("_ -> new")
    public static @NotNull Panel fromID(PanelID panelID) {
        return new Panel(panelID);
    }

    public void saveData() {
        final DataTraverser traverser = new DataTraverser(displayGroupID);
        final JsonObject mainSection = traverser.getData();
        final JsonObject attributeSection = new JsonObject();
        mainSection.add("hidden", new JsonPrimitive(hidden));
        this.attributes.forEach(
                (key, value) -> attributeSection.add(key, new JsonPrimitive(value.get().toString())));
        mainSection.add("attributes", attributeSection);
        traverser.save();
    }

    public PanelID getID() {
        return new PanelID(displayGroupID);
    }

    private DisplayGroup getDisplayGroup() {
        return DisplayGroup.fromUUID(displayGroupID.get());
    }

    @Contract("_ -> new")
    private @NotNull PanelAttribute getAttribute(String name) {
        return PanelAttribute.fromID(attributes.get(name));
    }

    public void setAttributeHidden(String name, boolean attributeHidden) {
        getAttribute(name).setHidden(attributeHidden);
        getAttribute(name).updateVisibility(hidden);
    }

    public void setText(String name, String text) {
        getAttribute(name).setText(text);
    }

    public void setHidden(boolean hidden) {
        if (this.hidden != hidden) {
            this.hidden = hidden;
            attributes.values().forEach(ID -> PanelAttribute.fromID(ID).updateVisibility(hidden));
            saveData();
        }
    }

    public void toggleHidden() {
        setHidden(!hidden);
    }

    public void remove() {
        attributes.values().forEach(ID -> PanelAttribute.fromID(ID).remove());
        getDisplayGroup().remove();
    }
}
