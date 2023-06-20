package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.connections.links.Link;

public class ConnectionPointOutput extends ConnectionPoint {
    public ConnectionPointOutput(String name, Location location) {
        super(name, location,
                Material.LIME_STAINED_GLASS,
                new Display.Brightness(15, 15),
                new Display.Brightness(3, 3));
    }

    public boolean hasLink() {
        return link != null;
    }

    @Override
    public void tick() {
        if (hasLink()) {
            link.tick();
        }
    }

    @Override
    public void remove() {
        blockDisplay.remove();
        interaction.remove();
        link.remove();
    }

    public void link(Link link) {
        if (this.link != null) {
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
