package org.metamechanists.quaptics.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.quaptics.connections.info.ConnectionInfoDisplay;
import org.metamechanists.quaptics.connections.links.Link;
import org.metamechanists.quaptics.utils.id.BlockDisplayID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;
import org.metamechanists.quaptics.utils.id.InteractionID;

import java.util.Map;

public class ConnectionPointInput extends ConnectionPoint {
    public ConnectionPointInput(String name, Location location) {
        super(name, location, Material.RED_CONCRETE, 15, 3);
    }

    private ConnectionPointInput(ConnectionPointID id, Link link, final String name, Location location, final int connectedBrightness, final int disconnectedBrightness,
                                 final ConnectionInfoDisplay infoDisplay, BlockDisplayID blockDisplay, InteractionID interaction) {
        super(id, link, name, location, connectedBrightness, disconnectedBrightness, infoDisplay, blockDisplay, interaction);
    }

    @Override
    public void tick() {}

    public static ConnectionPointInput deserialize(Map<String, Object> map) {
        return new ConnectionPointInput(
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
