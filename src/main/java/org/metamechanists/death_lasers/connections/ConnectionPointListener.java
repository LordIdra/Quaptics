package org.metamechanists.death_lasers.connections;

import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.metamechanists.death_lasers.storage.connections.ConnectionPointStorage;

public class ConnectionPointListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Interaction interaction) {
            final Location connectionPointLocation = interaction.getLocation();
            final ConnectionPointGroup group = ConnectionPointStorage.getConnectionGroupFromConnectionPointLocation(connectionPointLocation);
            final ConnectionPoint point = group.getConnectionPoint(interaction.getLocation());
            point.toggleConnected();
        }
    }
}
