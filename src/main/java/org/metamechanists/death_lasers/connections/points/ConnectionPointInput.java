package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Interaction;
import org.metamechanists.death_lasers.connections.info.ConnectionInfoDisplay;
import org.metamechanists.death_lasers.connections.links.Link;

import java.util.Map;
import java.util.UUID;

public class ConnectionPointInput extends ConnectionPoint {
    public ConnectionPointInput(String name, Location location) {
        super(name, location, Material.RED_CONCRETE, 15, 3);
    }

    private ConnectionPointInput(Link link, final String name, Location location, final int connectedBrightness, final int disconnectedBrightness,
                            final ConnectionInfoDisplay infoDisplay, BlockDisplay blockDisplay, Interaction interaction) {
        super(link, name, location, connectedBrightness, disconnectedBrightness, infoDisplay, blockDisplay, interaction);
    }

    @Override
    public void tick() {}

    public static ConnectionPointInput deserialize(Map<String, Object> map) {
        final Location location = (Location) map.get("location");
        final BlockDisplay blockDisplay = (BlockDisplay) location.getWorld().getEntity((UUID) map.get("blockDisplay"));
        final Interaction interaction = (Interaction) location.getWorld().getEntity((UUID) map.get("interaction"));
        return new ConnectionPointInput(
                (Link) map.get("link"),
                (String) map.get("name"),
                location,
                (int) map.get("connectedBrightness"),
                (int) map.get("disconnectedBrightness"),
                (ConnectionInfoDisplay) map.get("infoDisplay"),
                blockDisplay,
                interaction);
    }
}
