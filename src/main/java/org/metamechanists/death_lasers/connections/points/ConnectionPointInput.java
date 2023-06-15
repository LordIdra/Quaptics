package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;

public class ConnectionPointInput extends ConnectionPoint {
    private boolean linked;

    public ConnectionPointInput(Location location) {
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
