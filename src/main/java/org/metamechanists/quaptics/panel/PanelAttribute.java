package org.metamechanists.quaptics.panel;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.bakedlibs.dough.common.ChatColors;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.TextDisplayBuilder;
import org.metamechanists.quaptics.utils.id.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.TextDisplayId;

import java.util.Optional;

public class PanelAttribute {
    private static final float HIDDEN_VIEW_RANGE = 0;
    private static final float SHOWN_VIEW_RANGE = 1;

    @Getter
    private final TextDisplayId textDisplayId;
    private boolean hidden;

    public PanelAttribute(final Location location, final Vector3f displaySize) {
        this.textDisplayId = new TextDisplayId(new TextDisplayBuilder(location)
                .setTransformation(Transformations.unadjustedScale(displaySize))
                .setBrightness(15)
                .setViewRange(0)
                .setBillboard(Billboard.VERTICAL)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.hidden = false;
        saveData();
    }

    public PanelAttribute(final PanelAttributeId textDisplayId) {
        final DataTraverser traverser = new DataTraverser(textDisplayId);
        final JsonObject mainSection = traverser.getData();
        this.textDisplayId = new TextDisplayId(textDisplayId);
        this.hidden = mainSection.get("hidden").getAsBoolean();
    }

    private void saveData() {
        final DataTraverser traverser = new DataTraverser(textDisplayId);
        final JsonObject mainSection = traverser.getData();
        mainSection.add("hidden", new JsonPrimitive(hidden));
        traverser.save();
    }

    public PanelAttributeId getId() {
        return new PanelAttributeId(textDisplayId);
    }

    public void updateVisibility(final boolean panelHidden) {
        getTextDisplay().ifPresent(textDisplay -> textDisplay.setViewRange(hidden || panelHidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
    }

    private Optional<TextDisplay> getTextDisplay() {
        return textDisplayId.get();
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
        saveData();
    }

    public void setText(@NotNull final String text) {
        final Optional<TextDisplay> display = getTextDisplay();
        if (display.isEmpty()) {
            return;
        }

        final String currentText = display.get().getText();
        if (!text.equals(currentText)) {
            display.get().setText(ChatColors.color(text));
        }
    }

    public void remove() {
        getTextDisplay().ifPresent(Display::remove);
    }
}
