package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.utils.ConnectionPointLocation;

public class ConnectionPointInput extends ConnectionPoint {
    private boolean linked;

    public ConnectionPointInput(ConnectionPointLocation location) {
        super(location,
                Material.RED_STAINED_GLASS.createBlockData(),
                new Display.Brightness(15, 15),
                new Display.Brightness(3, 3),
                0.2F);
    }

    public boolean hasLink() {
        return linked;
    }

    public void link() {
        this.linked = true;
        blockDisplay.setBrightness(connectedBrightness);
    }

    public void unlink() {
        this.linked = false;
        blockDisplay.setBrightness(disconnectedBrightness);
    }
}
