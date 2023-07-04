package org.metamechanists.quaptics.panel;

import io.github.bakedlibs.dough.common.ChatColors;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.TextDisplayBuilder;
import org.metamechanists.quaptics.utils.id.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.TextDisplayId;

import java.util.Optional;

public class PanelAttribute {
    private static final float HIDDEN_VIEW_RANGE = 0;
    private static final float SHOWN_VIEW_RANGE = 1;
    private final Vector offset;
    @Getter
    private final TextDisplayId textDisplayId;
    private boolean hidden;

    public PanelAttribute(final @NotNull Location location, final Vector offset, final Vector3f displaySize) {
        this.textDisplayId = new TextDisplayId(new TextDisplayBuilder(location.add(offset))
                .setTransformation(Transformations.unadjustedScale(displaySize))
                .setBrightness(15)
                .setViewRange(0)
                .setBillboard(Billboard.VERTICAL)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.offset = offset;
        this.hidden = false;
        saveData();
    }

    public PanelAttribute(final PanelAttributeId textDisplayId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(textDisplayId);
        this.textDisplayId = new TextDisplayId(textDisplayId);
        this.offset = traverser.getVector("offset");
        this.hidden = traverser.getBoolean("hidden");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(textDisplayId);
        traverser.set("offset", offset);
        traverser.set("hidden", hidden);
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

    public void changeLocation(final Location location) {
        getTextDisplay().ifPresent(display -> display.teleport(location.clone().add(offset)));
    }

    public void setHidden(final boolean hidden) {
        if (this.hidden == hidden) {
            return;
        }

        this.hidden = hidden;
        saveData();
    }

    public void setText(@NotNull final String text) {
        getTextDisplay().ifPresent(display -> LegacyComponentSerializer.legacySection().serialize(Component.text(ChatColors.color(text))));
    }

    public void remove() {
        getTextDisplay().ifPresent(Display::remove);
    }
}
