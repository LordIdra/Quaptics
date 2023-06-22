package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.death_lasers.connections.info.ConnectionInfoDisplay;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.utils.id.BlockDisplayID;
import org.metamechanists.death_lasers.utils.id.ConnectionPointID;
import org.metamechanists.death_lasers.utils.id.InteractionID;

import java.util.Map;

public class ConnectionPointOutput extends ConnectionPoint {
    public ConnectionPointOutput(String name, Location location) {
        super(name, location, Material.LIME_CONCRETE, 15, 3);
    }

    private ConnectionPointOutput(ConnectionPointID id, Link link, final String name, Location location, final int connectedBrightness, final int disconnectedBrightness,
                                 final ConnectionInfoDisplay infoDisplay, BlockDisplayID blockDisplay, InteractionID interaction) {
        super(id, link, name, location, connectedBrightness, disconnectedBrightness, infoDisplay, blockDisplay, interaction);
    }

    @Override
    public void tick() {
        if (hasLink()) {
            link.tick();
        }
    }

    public static ConnectionPointOutput deserialize(Map<String, Object> map) {
        return new ConnectionPointOutput(
                (ConnectionPointID) map.get("id"),
                (Link) map.get("link"),
                (String) map.get("name"),
                (Location) map.get("location"),
                (int) map.get("connectedBrightness"),
                (int) map.get("disconnectedBrightness"),
                (ConnectionInfoDisplay) map.get("infoDisplay"),
                (BlockDisplayID) map.get("blockDisplayID"),
                (InteractionID) map.get("interactionID"));
    }
}
