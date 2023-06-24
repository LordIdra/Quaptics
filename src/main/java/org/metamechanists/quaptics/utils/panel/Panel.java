package org.metamechanists.quaptics.utils.panel;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.DisplayGroupID;

import java.util.HashMap;
import java.util.Map;

public class Panel implements ConfigurationSerializable {
    private static final float HIDDEN_VIEW_RANGE = 0;
    private static final float SHOWN_VIEW_RANGE = 15;
    private final DisplayGroupID displayGroupID;
    @Getter
    private boolean panelHidden = true;

    public Panel(DisplayGroupID displayGroupID) {
        this.displayGroupID = displayGroupID;
    }

    private Panel(DisplayGroupID displayGroupID, boolean panelHidden) {
        this.displayGroupID = displayGroupID;
        this.panelHidden = panelHidden;
    }

    private DisplayGroup getDisplayGroup() {
        return DisplayGroup.fromUUID(displayGroupID.get());
    }

    private TextDisplay getAttribute(String name) {
        return (TextDisplay) getDisplayGroup().getDisplays().get(name);
    }

    private void updateAttributeVisibility(String name) {
        final boolean attributeHidden = PersistentDataAPI.getBoolean(getAttribute(name), Keys.ATTRIBUTE_HIDDEN);
        getAttribute(name).setViewRange(panelHidden || attributeHidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE);
    }

    public void setPanelHidden(boolean panelHidden) {
        if (this.panelHidden != panelHidden) {
            getDisplayGroup().getDisplays().keySet().forEach(this::updateAttributeVisibility);
        }
    }

    public void toggleVisibility() {
        this.panelHidden = !panelHidden;
        getDisplayGroup().getDisplays().keySet().forEach(this::updateAttributeVisibility);
    }

    public void setAttributeHidden(String name, boolean hidden) {
        final boolean currentHidden = PersistentDataAPI.getBoolean(getAttribute(name), Keys.ATTRIBUTE_HIDDEN);
        if (currentHidden != hidden) {
            PersistentDataAPI.setBoolean(getAttribute(name), Keys.ATTRIBUTE_HIDDEN, hidden);
            updateAttributeVisibility(name);
        }
    }

    public void setText(String name, String text) {
        final String currentText = getAttribute(name).getText();
        if (!text.equals(currentText)) {
            getAttribute(name).setText(ChatColors.color(text));
        }
    }

    public void remove() {
        getDisplayGroup().getDisplays().values().forEach(Entity::remove);
        getDisplayGroup().remove();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("displayGroupID", displayGroupID);
        map.put("panelHidden", panelHidden);
        return map;
    }

    public static Panel deserialize(Map<String, Object> map) {
        return new Panel(
                (DisplayGroupID) map.get("displayGroupID"),
                (boolean) map.get("panelHidden"));
    }
}
