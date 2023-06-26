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
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.panels.PointPanel;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.BlockDisplayID;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;
import org.metamechanists.quaptics.utils.id.InteractionID;
import org.metamechanists.quaptics.utils.id.LinkID;
import org.metamechanists.quaptics.utils.id.PanelID;

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

    public ConnectionPoint(ConnectionGroupID groupID, String name, Location location, Material material, int connectedBrightness, int disconnectedBrightness) {
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

    protected ConnectionPoint(ConnectionPointID pointID) {
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

    protected void saveData(JsonObject mainSection) {
        mainSection.add("groupID", new JsonPrimitive(groupID.get().toString()));
        mainSection.add("blockDisplayID", new JsonPrimitive(blockDisplayID.get().toString()));
        mainSection.add("interactionID", new JsonPrimitive(interactionID.get().toString()));
        mainSection.add("panelID", new JsonPrimitive(panelID.get().toString()));
        mainSection.add("linkID", new JsonPrimitive(linkID == null ? "null" : linkID.get().toString()));
        mainSection.add("name", new JsonPrimitive(name));
        mainSection.add("connectedBrightness", new JsonPrimitive(connectedBrightness));
        mainSection.add("disconnectedBrightness", new JsonPrimitive(disconnectedBrightness));
    }

    protected abstract void saveData();

    public static ConnectionPoint fromID(ConnectionPointID id) {
        if (Bukkit.getEntity(id.get()) == null) { return null; }
        final DataTraverser traverser = new DataTraverser(id);
        final JsonObject mainSection = traverser.getData();
        final String type = mainSection.get("type").getAsString();
        return type.equals("output")
                ? new ConnectionPointOutput(id)
                : new ConnectionPointInput(id);
    }

    public ConnectionPointID getID() {
        return new ConnectionPointID(interactionID.get());
    }

    public boolean hasLink() {
        return linkID != null;
    }

    public Link getLink() {
        return Link.fromID(linkID);
    }

    public Location getLocation() {
        return getBlockDisplay().getLocation();
    }

    private PointPanel getPointPanel() {
        return new PointPanel(panelID, getID());
    }

    private BlockDisplay getBlockDisplay() {
        return (BlockDisplay) Bukkit.getEntity(blockDisplayID.get());
    }

    private Interaction getInteraction() {
        return (Interaction) Bukkit.getEntity(interactionID.get());
    }

    public ConnectionGroup getGroup() {
        return ConnectionGroup.fromID(groupID);
    }

    public void tick() {}

    public void remove() {
        if (hasLink()) {
            getLink().remove();
        }

        getPointPanel().remove();
        getBlockDisplay().remove();
        getInteraction().remove();
    }

    public void changeLocation(Location location) {
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

    public void togglePanelVisibility() {
        getPointPanel().toggleVisibility();
    }

    public void unlink() {
        linkID = null;
        getBlockDisplay().setBrightness(new Display.Brightness(disconnectedBrightness, 0));
        updatePanel();
        saveData();
    }

    public void link(LinkID linkID) {
        unlink();
        this.linkID = linkID;
        getBlockDisplay().setBrightness(new Display.Brightness(connectedBrightness, 0));
        updatePanel();
        saveData();
    }

    public void select() {
        getBlockDisplay().setGlowing(true);
        getBlockDisplay().setGlowColorOverride(Color.fromRGB(0, 255, 0));
    }

    public void deselect() {
        getBlockDisplay().setGlowing(false);
    }
}
