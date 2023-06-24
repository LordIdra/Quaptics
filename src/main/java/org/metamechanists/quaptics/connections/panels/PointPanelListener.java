package org.metamechanists.quaptics.connections.panels;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.connections.ConnectionPointStorage;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

public class PointPanelListener implements Listener {
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

        ConnectionPointStorage.getPoint(pointID).togglePanelVisibility();
    }
}
