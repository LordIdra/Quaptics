package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;

public class ConnectionPointOutput extends ConnectionPoint {
    private ConnectionPointInput target;

    public ConnectionPointOutput(Location location) {
        super(location,
                Material.LIME_STAINED_GLASS.createBlockData(),
                new Display.Brightness(15, 15),
                new Display.Brightness(3, 3),
                0.2F);
    }

    public boolean hasLink() {
        return target != null;
    }

    public void link(ConnectionPointInput target) {
        this.target = target;
        this.target.link();
        blockDisplay.setBrightness(connectedBrightness);
    }

    public void unlink() {
        target.unlink();
        target = null;
        blockDisplay.setBrightness(disconnectedBrightness);
    }
}
