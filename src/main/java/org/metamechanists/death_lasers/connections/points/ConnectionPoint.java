package org.metamechanists.death_lasers.connections.points;

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
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.info.ConnectionInfoDisplay;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.utils.DisplayUtils;
import org.metamechanists.death_lasers.utils.Keys;
import org.metamechanists.death_lasers.utils.id.BlockDisplayID;
import org.metamechanists.death_lasers.utils.id.ConnectionPointID;
import org.metamechanists.death_lasers.utils.id.InteractionID;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashMap;
import java.util.Map;

public abstract class ConnectionPoint implements ConfigurationSerializable {
    @Getter
    private final ConnectionPointID id;
    @Getter
    protected Link link;
    @Getter
    private final String name;
    @Getter
    protected Location location;
    @Getter
    protected final int connectedBrightness;
    @Getter
    protected final int disconnectedBrightness;
    private ConnectionInfoDisplay infoDisplay;
    protected final BlockDisplayID blockDisplayID;
    protected final InteractionID interactionID;
    protected final static float SCALE = 0.1F;
    public static final Vector INTERACTION_OFFSET = new Vector(0, -SCALE/2, 0);

    public ConnectionPoint(String name, Location location, Material material, int connectedBrightness, int disconnectedBrightness) {
        this.id = new ConnectionPointID();
        this.name = name;
        this.location = location;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.infoDisplay = new ConnectionInfoDisplay(id, true);

        final BlockDisplay blockDisplay = DisplayUtils.spawnBlockDisplay(location, material, DisplayUtils.simpleScaleTransformation(new Vector3f(SCALE, SCALE, SCALE)));
        this.blockDisplayID = new BlockDisplayID(blockDisplay.getUniqueId());

        final Interaction interaction = DisplayUtils.spawnInteraction(location.clone().add(INTERACTION_OFFSET), SCALE, SCALE);
        PersistentDataAPI.setString(interaction, Keys.CONNECTION_POINT_ID, id.toString());
        this.interactionID = new InteractionID(interaction.getUniqueId());

        getBlockDisplay().setBrightness(new Display.Brightness(disconnectedBrightness, 0));
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

    public void updateInfoDisplay() {
        infoDisplay.update();
    }

    public void remove() {
        if (hasLink()) {
            link.remove();
        }

        infoDisplay.remove();
        getBlockDisplay().remove();
        getInteraction().remove();
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

    public void updateLocation(Location location) {
        this.location = location;
        getBlockDisplay().teleport(location);
        getInteraction().teleport(location.clone().add(INTERACTION_OFFSET));
        boolean wasDisplayHidden = infoDisplay.isHidden();
        infoDisplay.remove();
        infoDisplay = new ConnectionInfoDisplay(id, wasDisplayHidden);
    }

    public void toggleInfoDisplayVisibility() {
        infoDisplay.toggleVisibility();
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
