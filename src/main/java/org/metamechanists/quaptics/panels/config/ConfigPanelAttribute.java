package org.metamechanists.quaptics.panels.config;

import io.github.bakedlibs.dough.common.ChatColors;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.builders.TextDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.simple.InteractionId;
import org.metamechanists.quaptics.utils.id.simple.TextDisplayId;

import java.util.Optional;

public class ConfigPanelAttribute {
    private static final float BUTTON_SIZE = 0.08F;
    private static final Vector BUTTON_ADJUSTMENT = new Vector(BUTTON_SIZE/2, BUTTON_SIZE/2, BUTTON_SIZE/2);
    private static final float HIDDEN_VIEW_RANGE = 0;
    private static final float SHOWN_VIEW_RANGE = 1;
    private static final Vector3f KEY_TRANSLATION = new Vector3f(-0.20F, 0.0F, 0.0F);
    private static final Vector3f SUB_TRANSLATION = new Vector3f(0.09F, 0.0F, 0.0F);
    private static final Vector3f VALUE_TRANSLATION = new Vector3f(0.27F, 0.0F, 0.0F);
    private static final Vector3f ADD_TRANSLATION = new Vector3f(0.45F, 0.0F, 0.0F);
    private final Vector offset;
    private final TextDisplayId keyId;
    private final TextDisplayId subId;
    private final InteractionId subButtonId;
    private final TextDisplayId valueId;
    private final TextDisplayId addId;
    private final InteractionId addButtonId;

    public ConfigPanelAttribute(final ConnectionGroupId groupId, @NotNull final String name, @NotNull final String key,
                                final @NotNull Location location, final Vector offset, final @NotNull Vector3f rotation, final Vector3f displaySize) {
        final Vector relativeSubButtonTranslation = new Vector(0.07, 0, -0.08).add(BUTTON_ADJUSTMENT).rotateAroundY(rotation.y);
        final Vector relativeAddButtonTranslation = new Vector(0.43, 0, -0.08).add(BUTTON_ADJUSTMENT).rotateAroundY(rotation.y);

        this.keyId = new TextDisplayId(new TextDisplayBuilder(location.clone().add(offset))
                .setTransformation(Transformations.unadjustedRotateTranslateScale(displaySize, rotation, KEY_TRANSLATION))
                .setText(ChatColors.color(key))
                .setBrightness(15)
                .setAlignment(TextAlignment.RIGHT)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.subId = new TextDisplayId(new TextDisplayBuilder(location.clone().add(offset))
                .setTransformation(Transformations.unadjustedRotateTranslateScale(displaySize, rotation, SUB_TRANSLATION))
                .setBrightness(15)
                .setText(ChatColors.color("&c-"))
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.valueId = new TextDisplayId(new TextDisplayBuilder(location.clone().add(offset))
                .setTransformation(Transformations.unadjustedRotateTranslateScale(displaySize, rotation, VALUE_TRANSLATION))
                .setBrightness(15)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());
        this.addId = new TextDisplayId(new TextDisplayBuilder(location.clone().add(offset))
                .setTransformation(Transformations.unadjustedRotateTranslateScale(displaySize, rotation, ADD_TRANSLATION))
                .setBrightness(15)
                .setText(ChatColors.color("&a+"))
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build().getUniqueId());

        final Interaction subButton = new InteractionBuilder(location.clone().add(offset).add(relativeSubButtonTranslation))
                .setWidth(BUTTON_SIZE)
                .setHeight(BUTTON_SIZE)
                .build();
        final Interaction addButton = new InteractionBuilder(location.clone().add(offset).add(relativeAddButtonTranslation))
                .setWidth(BUTTON_SIZE)
                .setHeight(BUTTON_SIZE)
                .build();

        final PersistentDataTraverser subButtonTraverser = new PersistentDataTraverser(subButton.getUniqueId());
        subButtonTraverser.set("groupId", groupId);
        subButtonTraverser.set("name", name);
        subButtonTraverser.set("buttonType", "sub");

        final PersistentDataTraverser addButtonTraverser = new PersistentDataTraverser(addButton.getUniqueId());
        addButtonTraverser.set("groupId", groupId);
        addButtonTraverser.set("name", name);
        addButtonTraverser.set("buttonType", "add");

        this.subButtonId = new InteractionId(subButton.getUniqueId());
        this.addButtonId = new InteractionId(addButton.getUniqueId());
        this.offset = offset;

        saveData();
    }

    public ConfigPanelAttribute(final ConfigPanelAttributeId keyId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(keyId);

        this.keyId = new TextDisplayId(keyId);
        this.offset = traverser.getVector("offset");
        this.subId = traverser.getTextDisplayId("subId");
        this.valueId = traverser.getTextDisplayId("valueId");
        this.addId = traverser.getTextDisplayId("addId");
        this.subButtonId = traverser.getInteractionId("subButtonId");
        this.addButtonId = traverser.getInteractionId("addButtonId");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(keyId);
        traverser.set("offset", offset);
        traverser.set("subId", subId);
        traverser.set("valueId", valueId);
        traverser.set("addId", addId);
        traverser.set("subButtonId", subButtonId);
        traverser.set("addButtonId", addButtonId);
    }

    public ConfigPanelAttributeId getId() {
        return new ConfigPanelAttributeId(keyId);
    }

    private Optional<TextDisplay> getKey() {
        return keyId.get();
    }
    private Optional<TextDisplay> getSub() {
        return subId.get();
    }
    private Optional<Interaction> getSubButton() {
        return subButtonId.get();
    }
    private Optional<TextDisplay> getValue() {
        return valueId.get();
    }
    private Optional<TextDisplay> getAdd() {
        return addId.get();
    }
    private Optional<Interaction> getAddButton() {
        return addButtonId.get();
    }

    public void changeLocation(final Location location) {
        getKey().ifPresent(display -> display.teleport(location.clone().add(offset)));
        getSub().ifPresent(display -> display.teleport(location.clone().add(offset)));
        getSubButton().ifPresent(display -> display.teleport(location.clone().add(offset)));
        getValue().ifPresent(display -> display.teleport(location.clone().add(offset)));
        getAdd().ifPresent(display -> display.teleport(location.clone().add(offset)));
        getAddButton().ifPresent(display -> display.teleport(location.clone().add(offset)));
    }

    public void setValue(@NotNull final String text) {
        getValue().ifPresent(display -> display.text(LegacyComponentSerializer.legacyAmpersand().deserialize(text)));
    }

    public void setHidden(final boolean hidden) {
        getKey().ifPresent(display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
        getSub().ifPresent(display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
        getSubButton().ifPresent(display -> display.setResponsive(hidden));
        getValue().ifPresent(display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
        getAdd().ifPresent(display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE));
        getAddButton().ifPresent(display -> display.setResponsive(hidden));
    }

    public void remove() {
        getKey().ifPresent(Display::remove);
        getSub().ifPresent(Display::remove);
        getSubButton().ifPresent(Interaction::remove);
        getValue().ifPresent(Display::remove);
        getAdd().ifPresent(Display::remove);
        getAddButton().ifPresent(Interaction::remove);
    }
}
