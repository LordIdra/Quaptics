package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Interaction;
import org.metamechanists.death_lasers.connections.info.ConnectionInfoDisplay;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.storage.SerializationUtils;

import java.util.Map;
import java.util.UUID;

public class ConnectionPointOutput extends ConnectionPoint {
    public ConnectionPointOutput(String name, Location location) {
        super(name, location, Material.LIME_CONCRETE, 15, 3);
    }

    private ConnectionPointOutput(Link link, final String name, Location location, final int connectedBrightness, final int disconnectedBrightness,
                                 final ConnectionInfoDisplay infoDisplay, BlockDisplay blockDisplay, Interaction interaction) {
        super(link, name, location, connectedBrightness, disconnectedBrightness, infoDisplay, blockDisplay, interaction);
    }

    @Override
    public void tick() {
        if (hasLink()) {
            link.tick();
        }
    }

    public static ConnectionPointOutput deserialize(Map<String, Object> map) {
        final Location location = (Location) map.get("location");
        final UUID blockDisplayUUID = SerializationUtils.deserializeUUID((Map<String, Object>) map.get("blockDisplay"));
        final BlockDisplay blockDisplay = (BlockDisplay) location.getWorld().getEntity(blockDisplayUUID);
        final Interaction interaction = (Interaction) location.getWorld().getEntity(SerializationUtils.deserializeUUID((Map<String, Object>) map.get("interaction")));
        return new ConnectionPointOutput(
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
