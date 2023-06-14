package org.metamechanists.death_lasers.connections;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.metamechanists.death_lasers.DEATH_LASERS;

public class ConnectionPoint {
    private final Location location;
    private final BlockDisplay blockDisplay;
    private final Interaction interaction;
    private final Display.Brightness connectedBrightness;
    private final Display.Brightness disconnectedBrightness;
    private Location target;

    public ConnectionPoint(Location location, ConnectionType type) {
        this.location = location;
        this.blockDisplay = type.buildBlockDisplay(location);
        this.interaction = type.buildInteraction(location);
        this.connectedBrightness = type.getConnectedBrightness();
        this.disconnectedBrightness = type.getDisconnectedBrightness();
    }

    public void remove() {
        blockDisplay.remove();
        interaction.remove();
    }

    public Location getLocation() {
        return location;
    }

    public boolean hasLink() {
        return target != null;
    }

    public void link(Location target) {
        this.target = target;
        blockDisplay.setBrightness(connectedBrightness);
        DEATH_LASERS.getInstance().getLogger().info("Linked");
    }

    public void unlink() {
        target = null;
        blockDisplay.setBrightness(disconnectedBrightness);
        DEATH_LASERS.getInstance().getLogger().info("Unlinked");
    }

    public void select() {
        blockDisplay.setGlowing(true);
        blockDisplay.setGlowColorOverride(Color.fromRGB(0, 255, 0));
    }

    public void deselect() {
        blockDisplay.setGlowing(false);
    }
}
