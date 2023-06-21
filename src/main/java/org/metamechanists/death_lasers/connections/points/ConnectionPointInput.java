package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;

public class ConnectionPointInput extends ConnectionPoint {
    public ConnectionPointInput(String name, Location location) {
        super(name, location,
                Material.RED_CONCRETE,
                new Display.Brightness(15, 0),
                new Display.Brightness(2, 0));
    }

    @Override
    public void tick() {}
}
