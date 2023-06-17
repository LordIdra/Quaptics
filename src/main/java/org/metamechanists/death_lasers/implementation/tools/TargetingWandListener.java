package org.metamechanists.death_lasers.implementation.tools;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

public class TargetingWandListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        final ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        final Location connectionPointLocation = clickedEntity.getLocation().clone().subtract(ConnectionPoint.INTERACTION_OFFSET);

        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        if (!ConnectionPointStorage.hasConnectionPoint(connectionPointLocation)) {
            return;
        }

        if (SlimefunItem.getByItem(heldItem) instanceof TargetingWand wand) {
            wand.use(event.getPlayer(), connectionPointLocation, heldItem);
        }
    }
}
