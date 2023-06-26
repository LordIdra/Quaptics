package org.metamechanists.quaptics.utils.panel;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
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

    private Panel(PanelID panelID) {
        final Entity e = Bukkit.getEntity(panelID.get());
        final DataTraverser traverser = new DataTraverser(panelID);
        final JsonObject mainSection = traverser.getData();
        final JsonObject attributeSection = mainSection.get("attributes").getAsJsonObject();
        this.displayGroupID = new DisplayGroupID(panelID.get());
        this.hidden = mainSection.get("hidden").getAsBoolean();
        this.attributes = new HashMap<>();
        attributeSection.asMap().forEach(
                (key, value) -> attributes.put(key, new PanelAttributeID(value.getAsString())));
    }

    public static Panel fromID(PanelID panelID) {
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
        return new PanelID(displayGroupID.get());
    }

    private DisplayGroup getDisplayGroup() {
        return DisplayGroup.fromUUID(displayGroupID.get());
    }

    private PanelAttribute getAttribute(String name) {
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
