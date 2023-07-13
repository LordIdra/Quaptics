package org.metamechanists.quaptics.panels.config;

import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.simple.DisplayGroupId;
import org.metamechanists.quaptics.utils.id.simple.InteractionId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelText;

import java.util.Optional;

public class ConfigPanelAttribute {
    private static final float BUTTON_SIZE = 0.08F;
    private static final Vector BUTTON_ADJUSTMENT = new Vector(BUTTON_SIZE/2, BUTTON_SIZE/2, BUTTON_SIZE/2);
    private static final float HIDDEN_VIEW_RANGE = 0;
    private static final float SHOWN_VIEW_RANGE = 1;
    private final Vector offset;
    private final DisplayGroupId displayGroupId;
    private final InteractionId subButtonId;
    private final InteractionId addButtonId;

    public ConfigPanelAttribute(final ConnectionGroupId groupId, @NotNull final String name, @NotNull final String key,
                                final @NotNull Location location, final Vector offset, final @NotNull Vector3d rotation, final float size) {
        final Vector subButtonLocation = new Vector(0.05, 0, -0.08).add(offset).add(BUTTON_ADJUSTMENT).rotateAroundY(rotation.y);
        final Vector addButtonLocation = new Vector(0.40, 0, -0.08).add(offset).add(BUTTON_ADJUSTMENT).rotateAroundY(rotation.y);

        this.displayGroupId = new DisplayGroupId(new ModelBuilder()
                .add("key", new ModelText()
                        .text(ChatColors.color(key))
                        .brightness(15)
                        .background(Color.fromARGB(0, 0, 0, 0))
                        .size(size)
                        .location(new Vector(-0.25, 0, 0).rotateAroundY(rotation.y).toVector3f())
                        .rotation(rotation))
                .add("value", new ModelText()
                        .brightness(15)
                        .background(Color.fromARGB(0, 0, 0, 0))
                        .size(size)
                        .location(new Vector(0.25, 0, 0).rotateAroundY(rotation.y).toVector3f())
                        .rotation(rotation))
                .add("sub", new ModelText()
                        .text(ChatColors.color("&c-"))
                        .brightness(15)
                        .background(Color.fromARGB(0, 0, 0, 0))
                        .size(size)
                        .location(new Vector(0.05, 0, 0).rotateAroundY(rotation.y).toVector3f())
                        .rotation(rotation))
                .add("add", new ModelText()
                        .text(ChatColors.color("&a+"))
                        .brightness(15)
                        .background(Color.fromARGB(0, 0, 0, 0))
                        .size(size)
                        .location(new Vector(0.40, 0, 0).rotateAroundY(rotation.y).toVector3f())
                        .rotation(rotation))
                .build(location.clone().add(offset))
                .getParentUUID());

//        this.keyId = new TextDisplayId(new TextDisplayBuilder()
//                .text(ChatColors.color(key))
//                .brightness(15)
//                .alignment(TextAlignment.RIGHT)
//                .backgroundColor(Color.fromARGB(0, 0, 0, 0))
//                .transformation(new TransformationMatrixBuilder()
//                        .scale(displaySize)
//                        .translate(KEY_TRANSLATION)
//                        .rotate(rotation)
//                        .buildForTextDisplay())
//                .build(location.clone().add(offset))
//                .getUniqueId());
//        this.subId = new TextDisplayId(new TextDisplayBuilder()
//                .brightness(15)
//                .text(ChatColors.color("&c-"))
//                .backgroundColor(Color.fromARGB(0, 0, 0, 0))
//                .transformation(new TransformationMatrixBuilder()
//                        .scale(displaySize)
//                        .translate(SUB_TRANSLATION)
//                        .rotate(rotation)
//                        .buildForTextDisplay())
//                .build(location.clone().add(offset))
//                .getUniqueId());
//        this.valueId = new TextDisplayId(new TextDisplayBuilder()
//                .brightness(15)
//                .backgroundColor(Color.fromARGB(0, 0, 0, 0))
//                .transformation(new TransformationMatrixBuilder()
//                        .scale(displaySize)
//                        .translate(VALUE_TRANSLATION)
//                        .rotate(rotation)
//                        .buildForTextDisplay())
//                .build(location.clone().add(offset))
//                .getUniqueId());
//        this.addId = new TextDisplayId(new TextDisplayBuilder()
//                .brightness(15)
//                .text(ChatColors.color("&a+"))
//                .backgroundColor(Color.fromARGB(0, 0, 0, 0))
//                .transformation(new TransformationMatrixBuilder()
//                        .scale(displaySize)
//                        .translate(ADD_TRANSLATION)
//                        .rotate(rotation)
//                        .buildForTextDisplay())
//                .build(location.clone().add(offset))
//                .getUniqueId());

        final Interaction subButton = new InteractionBuilder()
                .width(BUTTON_SIZE)
                .height(BUTTON_SIZE)
                .build(location.clone().add(subButtonLocation));
        final Interaction addButton = new InteractionBuilder()
                .width(BUTTON_SIZE)
                .height(BUTTON_SIZE)
                .build(location.clone().add(addButtonLocation));

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

    public ConfigPanelAttribute(final ConfigPanelAttributeId displayGroupId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroupId);

        this.displayGroupId = new DisplayGroupId(displayGroupId);
        this.offset = traverser.getVector("offset");
        this.subButtonId = traverser.getInteractionId("subButtonId");
        this.addButtonId = traverser.getInteractionId("addButtonId");
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroupId);
        traverser.set("offset", offset);
        traverser.set("subButtonId", subButtonId);
        traverser.set("addButtonId", addButtonId);
    }

    public ConfigPanelAttributeId getId() {
        return new ConfigPanelAttributeId(displayGroupId);
    }

    private Optional<TextDisplay> getValue() {
        if (displayGroupId.get().isEmpty()) {
            return Optional.empty();
        }

        return displayGroupId.get().get().getDisplays().get("value") instanceof final TextDisplay textDisplay
                ? Optional.of(textDisplay)
                : Optional.empty();
    }

    public void setValue(@NotNull final String text) {
        getValue().ifPresent(value -> value.setText(text));
    }

    public void setHidden(final boolean hidden) {
        displayGroupId.get().ifPresent(
                group -> group.getDisplays().values().forEach(
                        display -> display.setViewRange(hidden ? HIDDEN_VIEW_RANGE : SHOWN_VIEW_RANGE)));
    }

    public void remove() {
        displayGroupId.get().ifPresent(group -> {
            group.getDisplays().values().forEach(Entity::remove);
            group.remove();
        });
    }
}
