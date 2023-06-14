package org.metamechanists.death_lasers;

import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.metamechanists.death_lasers.implementation.ConnectionPoint;
import org.metamechanists.death_lasers.storage.connections.ConnectionPointGroup;
import org.metamechanists.death_lasers.storage.connections.ConnectionPointStorage;

public class ListenerTest implements Listener {

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
