package org.metamechanists.quaptics.panel;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.bakedlibs.dough.common.ChatColors;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.TextDisplayBuilder;
import org.metamechanists.quaptics.utils.id.PanelAttributeID;
import org.metamechanists.quaptics.utils.id.TextDisplayID;

public class PanelAttribute {
    private static final float HIDDEN_VIEW_RANGE = 0;
    private static final float SHOWN_VIEW_RANGE = 15;

    @Getter
    private final TextDisplayID textDisplayID;
    private boolean hidden;

    public PanelAttribute(Location location, Vector3f displaySize) {
        this.textDisplayID = new TextDisplayID(new TextDisplayBuilder(location)
                .setTransformation(Transformations.unadjustedScale(displaySize))
                .setBrightness(15)
                .setViewRange(0)
                .setBillboard(Display.Billboard.VERTICAL)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.hidden = false;
        saveData();
    }

    public PanelAttribute(PanelAttributeID textDisplayID) {
        final DataTraverser traverser = new DataTraverser(textDisplayID);
        final JsonObject mainSection = traverser.getData();
        this.textDisplayID = new TextDisplayID(textDisplayID);
        this.hidden = mainSection.get("hidden").getAsBoolean();
    }

    public void saveData() {
        final DataTraverser traverser = new DataTraverser(textDisplayID);
        final JsonObject mainSection = traverser.getData();
        mainSection.add("hidden", new JsonPrimitive(hidden));
        traverser.save();
    }

    public PanelAttributeID getID() {
        return new PanelAttributeID(textDisplayID);
    }

    public void updateVisibility(boolean panelHidden) {
        getTextDisplay().setViewRange(hidden || panelHidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE);
    }

    private TextDisplay getTextDisplay() {
        return (TextDisplay) Bukkit.getEntity(textDisplayID.getUUID());
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        saveData();
    }

    public void setText(@NotNull String text) {
        final String currentText = getTextDisplay().getText();
        if (!text.equals(currentText)) {
            getTextDisplay().setText(ChatColors.color(text));
        }
    }

    public void remove() {
        getTextDisplay().remove();
    }
}
