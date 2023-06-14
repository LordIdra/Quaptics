package org.metamechanists.death_lasers.implementation;

import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;

public class ConnectionPoint {
    private final Location location;
    private final BlockDisplay blockDisplay;
    private final Interaction interaction;
    private final Display.Brightness connectedBrightness;
    private final Display.Brightness disconnectedBrightness;
    private boolean connected = false;

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

    public void toggleConnected() {
        connected = !connected;
        blockDisplay.setBrightness(connected ? connectedBrightness : disconnectedBrightness);
    }
}
