package org.metamechanists.quaptics.connections.points;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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
    private final static float SIZE = 0.1F;
    private static final Vector INTERACTION_OFFSET = new Vector(0, -SIZE /2, 0);
    private final ConnectionGroupID groupID;
    @Getter
    private final InteractionID interactionID;
    private final BlockDisplayID blockDisplayID;
    private PanelID panelID;
    private LinkID linkID;
    @Getter
    private final String name;
    @Getter
    private final int connectedBrightness;
    @Getter
    private final int disconnectedBrightness;

    public ConnectionPoint(ConnectionGroupID groupID, String name, @NotNull Location location, Material material,
                           int connectedBrightness, int disconnectedBrightness) {
        final Interaction interaction = new InteractionBuilder(location.clone().add(INTERACTION_OFFSET))
                .setWidth(SIZE)
                .setHeight(SIZE)
                .build();
        this.groupID = groupID;
        this.interactionID = new InteractionID(interaction.getUniqueId());
        this.blockDisplayID = new BlockDisplayID(new BlockDisplayBuilder(location)
                .setMaterial(material)
                .setTransformation(Transformations.adjustedScale(new Vector3f(SIZE, SIZE, SIZE)))
                .setBrightness(disconnectedBrightness)
                .build()
                .getUniqueId());
        this.panelID = new PointPanel(location, getID()).getID();
        this.name = name;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        saveData();
        getPointPanel().update();
    }

    public ConnectionPoint(ConnectionPointID pointID) {
        final DataTraverser traverser = new DataTraverser(pointID);
        final JsonObject mainSection = traverser.getData();
        final String linkIDString = mainSection.get("linkID").getAsString();
        this.groupID = new ConnectionGroupID(mainSection.get("groupID").getAsString());
        this.blockDisplayID = new BlockDisplayID(mainSection.get("blockDisplayID").getAsString());
        this.interactionID = new InteractionID(mainSection.get("interactionID").getAsString());
        this.panelID = new PanelID(mainSection.get("panelID").getAsString());
        this.linkID = linkIDString.equals("null") ? null : new LinkID(linkIDString);
        this.name = mainSection.get("name").getAsString();
        this.connectedBrightness = mainSection.get("connectedBrightness").getAsInt();
        this.disconnectedBrightness = mainSection.get("disconnectedBrightness").getAsInt();
    }

    protected void saveData(@NotNull JsonObject mainSection) {
        mainSection.add("groupID", new JsonPrimitive(groupID.getUUID().toString()));
        mainSection.add("blockDisplayID", new JsonPrimitive(blockDisplayID.getUUID().toString()));
        mainSection.add("interactionID", new JsonPrimitive(interactionID.getUUID().toString()));
        mainSection.add("panelID", new JsonPrimitive(panelID.getUUID().toString()));
        mainSection.add("linkID", new JsonPrimitive(linkID == null ? "null" : linkID.getUUID().toString()));
        mainSection.add("name", new JsonPrimitive(name));
        mainSection.add("connectedBrightness", new JsonPrimitive(connectedBrightness));
        mainSection.add("disconnectedBrightness", new JsonPrimitive(disconnectedBrightness));
    }

    protected abstract void saveData();

    public ConnectionPointID getID() {
        return new ConnectionPointID(interactionID);
    }

    public boolean hasLink() {
        return linkID != null && getLink() != null;
    }

    public Link getLink() {
        return linkID.get();
    }

    public boolean isLinkEnabled() {
        return hasLink() && getLink().isEnabled();
    }

    public void disableLinkIfExists() {
        if (hasLink()) {
            getLink().setEnabled(false);
        }
    }

    public Location getLocation() {
        return getBlockDisplay().getLocation();
    }

    @Contract(" -> new")
    public @NotNull PointPanel getPointPanel() {
        return new PointPanel(panelID, getID());
    }

    private BlockDisplay getBlockDisplay() {
        return (BlockDisplay) Bukkit.getEntity(blockDisplayID.getUUID());
    }

    private Interaction getInteraction() {
        return (Interaction) Bukkit.getEntity(interactionID.getUUID());
    }

    public ConnectionGroup getGroup() {
        return groupID.get();
    }

    public void remove() {
        if (hasLink()) {
            getLink().remove();
        }

        getPointPanel().remove();
        getBlockDisplay().remove();
        getInteraction().remove();
    }

    public void changeLocation(@NotNull Location location) {
        getInteraction().teleport(location.clone().add(INTERACTION_OFFSET));
        getBlockDisplay().teleport(location);
        final boolean wasHidden = getPointPanel().isPanelHidden();
        getPointPanel().remove();
        panelID = new PointPanel(location, getID()).getID();
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
        linkID = null;
        getBlockDisplay().setBrightness(new Display.Brightness(disconnectedBrightness, 0));
        saveData();
        updatePanel();
    }

    public void link(LinkID linkID) {
        unlink();
        this.linkID = linkID;
        getBlockDisplay().setBrightness(new Display.Brightness(connectedBrightness, 0));
        saveData();
        updatePanel();
    }

    public void select() {
        getBlockDisplay().setGlowing(true);
        getBlockDisplay().setGlowColorOverride(Color.fromRGB(0, 255, 0));
    }

    public void deselect() {
        getBlockDisplay().setGlowing(false);
    }
}
