package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;

public class ConnectionPointOutput extends ConnectionPoint {
    public ConnectionPointOutput(String name, Location location) {
        super(name, location,
                Material.LIME_CONCRETE,
                new Display.Brightness(15, 0),
                new Display.Brightness(3, 0));
    }

    @Override
    public void tick() {
        if (hasLink()) {
            link.tick();
        }
    }
}
