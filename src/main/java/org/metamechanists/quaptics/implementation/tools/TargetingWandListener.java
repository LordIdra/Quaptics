package org.metamechanists.quaptics.implementation.tools;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

public class TargetingWandListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        final ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (!(SlimefunItem.getByItem(heldItem) instanceof TargetingWand wand)) {
            return;
        }

        final ConnectionPointID pointID = new ConnectionPointID(PersistentDataAPI.getString(clickedEntity, Keys.CONNECTION_POINT_ID));
        if (ConnectionPoint.fromID(pointID) == null) {
            return;
        }

        wand.use(event.getPlayer(), pointID, heldItem);
    }

    @EventHandler
    public void scrollEvent(PlayerItemHeldEvent event) {
        final ItemStack heldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (SlimefunItem.getByItem(heldItem) instanceof TargetingWand wand) {
            wand.unsetSource(heldItem);
        }
    }
}
