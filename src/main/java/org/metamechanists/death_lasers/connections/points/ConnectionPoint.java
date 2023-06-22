package org.metamechanists.death_lasers.connections.points;

import lombok.Getter;
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
import org.metamechanists.death_lasers.connections.info.ConnectionInfoDisplay;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.storage.ConnectionPointStorage;
import org.metamechanists.death_lasers.utils.DisplayUtils;
import org.metamechanists.death_lasers.utils.SerializationUtils;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashMap;
import java.util.Map;

public abstract class ConnectionPoint implements ConfigurationSerializable {
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
    protected BlockDisplay blockDisplay;
    protected Interaction interaction;
    protected final static float SCALE = 0.1F;
    public static Vector INTERACTION_OFFSET = new Vector(0, -SCALE/2, 0);

    public ConnectionPoint(String name, Location location, Material material, int connectedBrightness, int disconnectedBrightness) {
        this.name = name;
        this.location = location;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.infoDisplay = new ConnectionInfoDisplay(this);
        this.blockDisplay = DisplayUtils.spawnBlockDisplay(location, material,
                DisplayUtils.simpleScaleTransformation(new Vector3f(SCALE, SCALE, SCALE)));
        this.interaction = DisplayUtils.spawnInteraction(location.clone().add(INTERACTION_OFFSET), SCALE, SCALE);
        this.blockDisplay.setBrightness(new Display.Brightness(disconnectedBrightness, 0));
    }

    protected ConnectionPoint(Link link, final String name, Location location, final int connectedBrightness, final int disconnectedBrightness,
                              final ConnectionInfoDisplay infoDisplay, BlockDisplay blockDisplay, Interaction interaction) {
        this.link = link;
        this.name = name;
        this.location = location;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.infoDisplay = infoDisplay;
        this.blockDisplay = blockDisplay;
        this.interaction = interaction;
    }

    public ConnectionGroup getGroup() {
        return ConnectionPointStorage.getGroup(location);
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
        blockDisplay.remove();
        interaction.remove();
    }

    public boolean hasLink() {
        return link != null;
    }

    public void link(Link link) {
        if (hasLink()) {
            unlink();
        }
        this.link = link;
        blockDisplay.setBrightness(new Display.Brightness(connectedBrightness, 0));
        updateInfoDisplay();
    }

    public void unlink() {
        link = null;
        blockDisplay.setBrightness(new Display.Brightness(disconnectedBrightness, 0));
        updateInfoDisplay();
    }

    public void select() {
        blockDisplay.setGlowing(true);
        blockDisplay.setGlowColorOverride(Color.fromRGB(0, 255, 0));
    }

    public void deselect() {
        blockDisplay.setGlowing(false);
    }

    public void updateLocation(Location location) {
        this.location = location;
        blockDisplay.teleport(location);
        interaction.teleport(location.clone().add(INTERACTION_OFFSET));
        infoDisplay.remove();
        infoDisplay = new ConnectionInfoDisplay(this);
    }

    public void toggleInfoDisplayVisibility() {
        infoDisplay.toggleVisibility();
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("link", link);
        map.put("name", name);
        map.put("location", location);
        map.put("connectedBrightness", connectedBrightness);
        map.put("disconnectedBrightness", disconnectedBrightness);
        map.put("infoDisplay", infoDisplay);
        map.put("blockDisplay", SerializationUtils.serializeUUID(blockDisplay.getUniqueId()));
        map.put("interaction", SerializationUtils.serializeUUID(interaction.getUniqueId()));
        return map;
    }
}
