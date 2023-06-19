package org.metamechanists.death_lasers.connections.points;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;

public class ConnectionPointInput extends ConnectionPoint {
    @Getter
    private ConnectionPointOutput source;

    public ConnectionPointInput(String name, Location location) {
        super(name, location,
                Material.RED_STAINED_GLASS,
                new Display.Brightness(15, 15),
                new Display.Brightness(2, 2));
    }


    @Override
    public void tick() {}

    @Override
    public void remove() {
        blockDisplay.remove();
        interaction.remove();
        if (source != null) {
            if (source.hasLink()) {
                source.unlink();
            }
        }
    }


    public boolean hasLink() {
        return source != null;
    }

    public void link(ConnectionPointOutput source) {
        this.source = source;
        blockDisplay.setBrightness(connectedBrightness);
        final ConnectionGroup sourceGroup = ConnectionPointStorage.getGroupFromPointLocation(source.getLocation());
        sourceGroup.getBlock().onNodeUpdated(source);
    }

    public void unlink() {
        this.source = null;
        blockDisplay.setBrightness(disconnectedBrightness);
    }
}
