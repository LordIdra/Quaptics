package org.metamechanists.death_lasers.connections.points;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;

public class ConnectionPointInput extends ConnectionPoint {
    @Getter
    private Location source;

    public ConnectionPointInput(Location location) {
        super(location,
                Material.RED_STAINED_GLASS.createBlockData(),
                new Display.Brightness(15, 15),
                new Display.Brightness(3, 3));
    }

    @Override
    public void tick() {}

    @Override
    public void remove() {
        blockDisplay.remove();
        interaction.remove();
        if (source != null) {
            ConnectionPointOutput sourcePoint = (ConnectionPointOutput) ConnectionPointStorage.getPointFromPointLocation(source);
            if (sourcePoint.hasLink()) {
                sourcePoint.unlink();
            }
        }
    }

    @Override
    public void kill() {}

    public boolean hasLink() {
        return source != null;
    }

    public void link(Location source) {
        this.source = source;
        blockDisplay.setBrightness(connectedBrightness);
    }

    public void unlink() {
        this.source = null;
        blockDisplay.setBrightness(disconnectedBrightness);
    }
}
