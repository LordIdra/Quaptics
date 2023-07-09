package org.metamechanists.quaptics.panels.info;

import lombok.Getter;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.builders.TextDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelAttributeId;
import org.metamechanists.quaptics.utils.id.simple.TextDisplayId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import java.util.Optional;

public class InfoPanelAttribute {
    private static final float HIDDEN_VIEW_RANGE = 0;
    private static final float SHOWN_VIEW_RANGE = 1;
    @Getter
    private final TextDisplayId id;
    private boolean hidden;

    public InfoPanelAttribute(final @NotNull Location location, final Vector3f displaySize) {
        this.id = new TextDisplayId(new TextDisplayBuilder(location)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(displaySize)
                        .buildForTextDisplay())
                .setBrightness(15)
                .setViewRange(0)
                .setBillboard(Billboard.VERTICAL)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.hidden = false;
        saveData();
    }

    public InfoPanelAttribute(final InfoPanelAttributeId id) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        this.id = new TextDisplayId(id);
        this.hidden = traverser.getBoolean("hidden");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        traverser.set("hidden", hidden);
    }

    public InfoPanelAttributeId getId() {
        return new InfoPanelAttributeId(id);
    }

    public void updateVisibility(final boolean panelHidden) {
        getTextDisplay().ifPresent(textDisplay -> textDisplay.setViewRange(hidden || panelHidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
    }

    private Optional<TextDisplay> getTextDisplay() {
        return id.get();
    }

    public void changeLocation(final Location location) {
        getTextDisplay().ifPresent(display -> display.teleport(location.clone()));
    }

    public void setHidden(final boolean hidden) {
        if (this.hidden == hidden) {
            return;
        }

        this.hidden = hidden;
        saveData();
    }

    public void setText(@NotNull final String text) {
        getTextDisplay().ifPresent(display -> display.text(LegacyComponentSerializer.legacyAmpersand().deserialize(text)));
    }

    public void remove() {
        getTextDisplay().ifPresent(Display::remove);
    }
}
