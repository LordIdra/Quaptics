package org.metamechanists.death_lasers.connections;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.Items;
import org.metamechanists.death_lasers.storage.connections.ConnectionPointStorage;

public class ConnectionPointListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        final ItemStack heldItem = event.getPlayer().getActiveItem();

        if (!(clickedEntity instanceof Interaction interaction) || !(SlimefunUtils.isItemSimilar(heldItem, Items.TARGETING_WAND, false))) {
            return;
        }

        if (!Items.targetingWand.canUse(event.getPlayer(), false)
            || !Slimefun.getProtectionManager().hasPermission(event.getPlayer(), event.getPlayer().getLocation(),
                    io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction.INTERACT_BLOCK)) {
            return;
        }

        final Location connectionPointLocation = interaction.getLocation();
        final ConnectionPointGroup group = ConnectionPointStorage.getConnectionGroupFromConnectionPointLocation(connectionPointLocation);
        final ConnectionPoint point = group.getConnectionPoint(interaction.getLocation());

        point.toggleConnected();
    }
}
