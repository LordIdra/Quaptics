package org.metamechanists.death_lasers.connections.info;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.implementation.tools.TargetingWand;
import org.metamechanists.death_lasers.utils.Keys;
import org.metamechanists.death_lasers.utils.id.ConnectionPointID;

public class PointInformationListener implements Listener {
    @EventHandler
    public void interactEvent(PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        final ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (SlimefunItem.getByItem(heldItem) instanceof TargetingWand) {
            return;
        }

        final ConnectionPointID pointID = new ConnectionPointID(PersistentDataAPI.getString(clickedEntity, Keys.CONNECTION_POINT_ID));
        if (!ConnectionPointStorage.hasPoint(pointID)) {
            return;
        }

        ConnectionPointStorage.getPoint(pointID).toggleInfoDisplayVisibility();
    }
}
