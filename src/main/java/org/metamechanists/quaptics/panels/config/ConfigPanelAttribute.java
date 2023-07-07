package org.metamechanists.quaptics.panels.config;

import io.github.bakedlibs.dough.common.ChatColors;
import lombok.Getter;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.TextDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelAttributeId;
import org.metamechanists.quaptics.utils.id.simple.TextDisplayId;

import java.util.Optional;

public class ConfigPanelAttribute {
    private static final float HIDDEN_VIEW_RANGE = 0;
    private static final float SHOWN_VIEW_RANGE = 1;
    private static final Vector3f keyTranslation = new Vector3f(-0.2F, 0.0F, 0.0F);
    private static final Vector3f valueTranslation = new Vector3f(0.2F, 0.0F, 0.0F);
    private static final Vector3f subTranslation = new Vector3f(0.4F, 0.0F, 0.0F);
    private static final Vector3f addTranslation = new Vector3f(0.3F, 0.0F, 0.0F);
    private final Vector offset;
    private final TextDisplayId keyId;
    private final TextDisplayId valueId;
    @Getter
    private final TextDisplayId addId;
    private final TextDisplayId subId;

    public ConfigPanelAttribute(@NotNull final String key, final @NotNull Location location, final Vector offset, final Vector3f rotation, final Vector3f displaySize) {
        this.keyId = new TextDisplayId(new TextDisplayBuilder(location.clone().add(offset))
                .setTransformation(Transformations.unadjustedTranslateRotateScale(displaySize, rotation, keyTranslation))
                .setBrightness(15)
                .setAlignment(TextAlignment.LEFT)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.valueId = new TextDisplayId(new TextDisplayBuilder(location.clone().add(offset))
                .setTransformation(Transformations.unadjustedTranslateRotateScale(displaySize, rotation, valueTranslation))
                .setBrightness(15)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.subId = new TextDisplayId(new TextDisplayBuilder(location.clone().add(offset))
                .setTransformation(Transformations.unadjustedTranslateRotateScale(displaySize, rotation, subTranslation))
                .setBrightness(15)
                .setText(ChatColors.color("&c-"))
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.addId = new TextDisplayId(new TextDisplayBuilder(location.clone().add(offset))
                .setTransformation(Transformations.unadjustedTranslateRotateScale(displaySize, rotation, addTranslation))
                .setBrightness(15)
                .setText(ChatColors.color("&a+"))
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.offset = offset;
        saveData();
    }

    public ConfigPanelAttribute(final ConfigPanelAttributeId keyId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(keyId);
        this.keyId = new TextDisplayId(keyId);
        this.offset = traverser.getVector("offset");
        this.valueId = traverser.getTextDisplayId("valueId");
        this.subId = traverser.getTextDisplayId("subId");
        this.addId = traverser.getTextDisplayId("addId");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(keyId);
        traverser.set("offset", offset);
        traverser.set("valueId", valueId);
        traverser.set("subId", subId);
        traverser.set("addId", addId);
    }

    public ConfigPanelAttributeId getId() {
        return new ConfigPanelAttributeId(keyId);
    }

    private Optional<TextDisplay> getKey() {
        return keyId.get();
    }

    private Optional<TextDisplay> getValue() {
        return keyId.get();
    }

    private Optional<TextDisplay> getSub() {
        return keyId.get();
    }

    private Optional<TextDisplay> getAdd() {
        return keyId.get();
    }

    public void changeLocation(final Location location) {
        getKey().ifPresent(display -> display.teleport(location.clone().add(offset)));
        getValue().ifPresent(display -> display.teleport(location.clone().add(offset)));
        getSub().ifPresent(display -> display.teleport(location.clone().add(offset)));
        getAdd().ifPresent(display -> display.teleport(location.clone().add(offset)));
    }

    public void setValue(@NotNull final String text) {
        getValue().ifPresent(display -> display.text(LegacyComponentSerializer.legacyAmpersand().deserialize(text)));
    }

    public void setHidden(final boolean hidden) {
        getKey().ifPresent(display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
        getValue().ifPresent(display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
        getSub().ifPresent(display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
        getAdd().ifPresent(display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
    }

    public void remove() {
        getKey().ifPresent(Display::remove);
        getValue().ifPresent(Display::remove);
        getSub().ifPresent(Display::remove);
        getAdd().ifPresent(Display::remove);
    }
}
