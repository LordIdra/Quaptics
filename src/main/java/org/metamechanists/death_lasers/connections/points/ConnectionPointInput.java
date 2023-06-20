package org.metamechanists.death_lasers.connections.points;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.connections.links.Link;

public class ConnectionPointInput extends ConnectionPoint {
    public ConnectionPointInput(String name, Location location) {
        super(name, location,
                Material.RED_STAINED_GLASS,
                new Display.Brightness(15, 15),
                new Display.Brightness(2, 2));
    }

    public boolean hasLink() {
        return link != null;
    }

    @Override
    public void tick() {}

    @Override
    public void remove() {
        blockDisplay.remove();
        interaction.remove();
        if (hasLink()) {
            link.remove();
        }
    }

    public void link(Link link) {
        if (hasLink()) {
            unlink();
        }
        this.link = link;
        blockDisplay.setBrightness(connectedBrightness);
    }

    public void unlink() {
        link = null;
        blockDisplay.setBrightness(disconnectedBrightness);
    }
}
