package org.metamechanists.quaptics.connections.points;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.panels.PointPanel;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.*;

public abstract class ConnectionPoint {
    private static final float SIZE = 0.1F;
    private static final Vector INTERACTION_OFFSET = new Vector(0, -SIZE /2, 0);
    private final ConnectionGroupId groupId;
    @Getter
    private final InteractionId interactionId;
    private final BlockDisplayId blockDisplayId;
    private PanelId panelId;
    private LinkId linkId;
    @Getter
    private final String name;
    @Getter
    private final int connectedBrightness;
    @Getter
    private final int disconnectedBrightness;

    protected ConnectionPoint(ConnectionGroupId groupId, String name, @NotNull Location location, Material material,
                              int connectedBrightness, int disconnectedBrightness) {
        final Interaction interaction = new InteractionBuilder(location.clone().add(INTERACTION_OFFSET))
                .setWidth(SIZE)
                .setHeight(SIZE)
                .build();
        this.groupId = groupId;
        this.interactionId = new InteractionId(interaction.getUniqueId());
        this.blockDisplayId = new BlockDisplayId(new BlockDisplayBuilder(location)
                .setMaterial(material)
                .setTransformation(Transformations.adjustedScale(new Vector3f(SIZE, SIZE, SIZE)))
                .setBrightness(disconnectedBrightness)
                .build()
                .getUniqueId());
        this.panelId = new PointPanel(location, getId()).getId();
        this.name = name;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        saveData();
        getPointPanel().update();
    }

    protected ConnectionPoint(ConnectionPointId pointId) {
        final DataTraverser traverser = new DataTraverser(pointId);
        final JsonObject mainSection = traverser.getData();
        final String linkIdString = mainSection.get("linkId").getAsString();
        this.groupId = new ConnectionGroupId(mainSection.get("groupId").getAsString());
        this.blockDisplayId = new BlockDisplayId(mainSection.get("blockDisplayId").getAsString());
        this.interactionId = new InteractionId(mainSection.get("interactionId").getAsString());
        this.panelId = new PanelId(mainSection.get("panelId").getAsString());
        this.linkId = linkIdString.equals("null") ? null : new LinkId(linkIdString);
        this.name = mainSection.get("name").getAsString();
        this.connectedBrightness = mainSection.get("connectedBrightness").getAsInt();
        this.disconnectedBrightness = mainSection.get("disconnectedBrightness").getAsInt();
    }

    protected void saveData(@NotNull JsonObject mainSection) {
        mainSection.add("groupId", new JsonPrimitive(groupId.getUUID().toString()));
        mainSection.add("blockDisplayId", new JsonPrimitive(blockDisplayId.getUUID().toString()));
        mainSection.add("interactionId", new JsonPrimitive(interactionId.getUUID().toString()));
        mainSection.add("panelId", new JsonPrimitive(panelId.getUUID().toString()));
        mainSection.add("linkId", new JsonPrimitive(linkId == null ? "null" : linkId.getUUID().toString()));
        mainSection.add("name", new JsonPrimitive(name));
        mainSection.add("connectedBrightness", new JsonPrimitive(connectedBrightness));
        mainSection.add("disconnectedBrightness", new JsonPrimitive(disconnectedBrightness));
    }

    protected abstract void saveData();

    public @NotNull ConnectionPointId getId() {
        return new ConnectionPointId(interactionId);
    }

    public boolean hasLink() {
        return linkId != null && getLink() != null;
    }

    public @Nullable Link getLink() {
        return linkId.get();
    }

    public boolean isLinkEnabled() {
        return hasLink() && getLink().isEnabled();
    }

    public void disableLinkIfExists() {
        if (hasLink()) {
            getLink().setEnabled(false);
        }
    }

    public @Nullable Location getLocation() {
        final BlockDisplay blockDisplay = getBlockDisplay();
        return blockDisplay != null
                ? blockDisplay.getLocation()
                : null;
    }

    @Contract(" -> new")
    public @NotNull PointPanel getPointPanel() {
        return new PointPanel(panelId, getId());
    }

    private @Nullable BlockDisplay getBlockDisplay() {
        return blockDisplayId.get();
    }

    private @Nullable Interaction getInteraction() {
        return interactionId.get();
    }

    public @Nullable ConnectionGroup getGroup() {
        return groupId.get();
    }

    public void remove() {
        if (hasLink()) {
            getLink().remove();
        }

        getPointPanel().remove();

        final BlockDisplay blockDisplay = getBlockDisplay();
        if (blockDisplay != null) {
            blockDisplay.remove();
        }

        final Interaction interaction = getInteraction();
        if (interaction != null) {
            interaction.remove();
        }
    }

    public void changeLocation(@NotNull Location location) {
        final Interaction interaction = getInteraction();
        final BlockDisplay blockDisplay = getBlockDisplay();
        if (interaction == null || blockDisplay == null) {
            return;
        }


        blockDisplay.teleport(location);
        interaction.teleport(location.clone().add(INTERACTION_OFFSET));

        final boolean wasHidden = getPointPanel().isPanelHidden();
        getPointPanel().remove();

        this.panelId = new PointPanel(location, getId()).getId();
        getPointPanel().setPanelHidden(wasHidden);
        saveData();
    }

    public void updatePanel() {
        getPointPanel().update();
    }

    public void togglePanelHidden() {
        getPointPanel().toggleHidden();
    }

    public void unlink() {
        this.linkId = null;

        final BlockDisplay blockDisplay = getBlockDisplay();
        if (blockDisplay != null) {
            blockDisplay.setBrightness(new Display.Brightness(this.disconnectedBrightness, 0));
        }

        saveData();
        updatePanel();
    }

    public void link(LinkId linkId) {
        unlink();
        this.linkId = linkId;

        final BlockDisplay blockDisplay = getBlockDisplay();
        if (blockDisplay != null) {
            blockDisplay.setBrightness(new Display.Brightness(this.connectedBrightness, 0));
        }

        saveData();
        updatePanel();
    }

    public void select() {
        final BlockDisplay blockDisplay = getBlockDisplay();
        if (blockDisplay != null) {
            blockDisplay.setGlowing(true);
            blockDisplay.setGlowColorOverride(Color.fromRGB(0, 255, 0));
        }
    }

    public void deselect() {
        final BlockDisplay blockDisplay = getBlockDisplay();
        if (blockDisplay != null) {
            blockDisplay.setGlowing(false);
        }
    }
}
