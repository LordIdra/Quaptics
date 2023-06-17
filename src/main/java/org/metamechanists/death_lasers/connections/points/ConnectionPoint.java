package org.metamechanists.death_lasers.connections.points;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public abstract class ConnectionPoint {
    @Getter
    private final String name;
    @Getter
    protected Location location;
    @Getter
    protected final Display.Brightness connectedBrightness;
    @Getter
    protected final Display.Brightness disconnectedBrightness;
    protected BlockDisplay blockDisplay;
    protected Interaction interaction;

    ConnectionPoint(String name, Location location, Material material, Display.Brightness connectedBrightness, Display.Brightness disconnectedBrightness) {
        this.name = name;
        this.location = location;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.blockDisplay = DisplayUtils.spawnBlockDisplay(location, material, DisplayUtils.simpleScaleTransformation(new Vector3f(0.2F, 0.2F, 0.2F)));
        this.interaction = DisplayUtils.spawnInteraction(location, 0.2F, 0.2F);
        this.blockDisplay.setBrightness(disconnectedBrightness);
    }

    public abstract void tick();
    public abstract void remove();
    public abstract void killBeam();

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
        interaction.teleport(location);
    }
}
