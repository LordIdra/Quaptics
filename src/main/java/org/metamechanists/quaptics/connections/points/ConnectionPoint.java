package org.metamechanists.quaptics.connections.points;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPointStorage;
import org.metamechanists.quaptics.connections.info.ConnectionInfoDisplay;
import org.metamechanists.quaptics.connections.links.Link;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.BlockDisplayID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;
import org.metamechanists.quaptics.utils.id.InteractionID;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashMap;
import java.util.Map;

public abstract class ConnectionPoint implements ConfigurationSerializable {
    protected final static float SIZE = 0.1F;
    private static final Vector INTERACTION_OFFSET = new Vector(0, -SIZE /2, 0);
    @Getter
    private final ConnectionPointID id;
    protected final BlockDisplayID blockDisplayID;
    protected final InteractionID interactionID;
    @Getter
    private final String name;
    @Getter
    protected final int connectedBrightness;
    @Getter
    protected final int disconnectedBrightness;
    @Getter
    protected Link link;
    @Getter
    protected Location location;
    private ConnectionInfoDisplay infoDisplay;

    public ConnectionPoint(String name, Location location, Material material, int connectedBrightness, int disconnectedBrightness) {
        this.id = new ConnectionPointID();
        this.name = name;
        this.location = location;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.infoDisplay = new ConnectionInfoDisplay(id, location, true);
        this.blockDisplayID = new BlockDisplayID(new BlockDisplayBuilder(location)
                .setMaterial(material)
                .setTransformation(Transformations.scale(new Vector3f(SIZE, SIZE, SIZE)))
                .setBrightness(disconnectedBrightness)
                .build()
                .getUniqueId());
        final Interaction interaction = new InteractionBuilder(location.clone().add(INTERACTION_OFFSET))
                .setWidth(SIZE)
                .setHeight(SIZE)
                .build();
        PersistentDataAPI.setString(interaction, Keys.CONNECTION_POINT_ID, id.toString());
        this.interactionID = new InteractionID(interaction.getUniqueId());
    }

    protected ConnectionPoint(ConnectionPointID id, Link link, final String name, Location location, final int connectedBrightness, final int disconnectedBrightness,
                              final ConnectionInfoDisplay infoDisplay, BlockDisplayID blockDisplayID, InteractionID interactionID) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.location = location;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.infoDisplay = infoDisplay;
        this.blockDisplayID = blockDisplayID;
        this.interactionID = interactionID;
    }

    private BlockDisplay getBlockDisplay() {
        return (BlockDisplay) Bukkit.getEntity(blockDisplayID.get());
    }

    private Interaction getInteraction() {
        return (Interaction) Bukkit.getEntity(interactionID.get());
    }

    public ConnectionGroup getGroup() {
        return ConnectionPointStorage.getGroup(id);
    }

    public abstract void tick();

    public void remove() {
        if (hasLink()) {
            link.remove();
        }

        infoDisplay.remove();
        getBlockDisplay().remove();
        getInteraction().remove();
    }

    public void changeLocation(Location location) {
        this.location = location;
        getBlockDisplay().teleport(location);
        getInteraction().teleport(location.clone().add(INTERACTION_OFFSET));
        boolean wasDisplayHidden = infoDisplay.isHidden();
        infoDisplay.remove();
        infoDisplay = new ConnectionInfoDisplay(id, location, wasDisplayHidden);
    }

    public void updateInfoDisplay() {
        infoDisplay.update();
    }

    public void toggleInfoDisplayVisibility() {
        infoDisplay.toggleVisibility();
    }

    public boolean hasLink() {
        return link != null;
    }

    public void link(Link link) {
        if (hasLink()) {
            unlink();
        }
        this.link = link;
        getBlockDisplay().setBrightness(new Display.Brightness(connectedBrightness, 0));
        updateInfoDisplay();
    }

    public void unlink() {
        link = null;
        getBlockDisplay().setBrightness(new Display.Brightness(disconnectedBrightness, 0));
        updateInfoDisplay();
    }

    public void select() {
        getBlockDisplay().setGlowing(true);
        getBlockDisplay().setGlowColorOverride(Color.fromRGB(0, 255, 0));
    }

    public void deselect() {
        getBlockDisplay().setGlowing(false);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("link", link);
        map.put("name", name);
        map.put("location", location);
        map.put("connectedBrightness", connectedBrightness);
        map.put("disconnectedBrightness", disconnectedBrightness);
        map.put("infoDisplay", infoDisplay);
        map.put("blockDisplayID", blockDisplayID);
        map.put("interactionID", interactionID);
        return map;
    }
}
