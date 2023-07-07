package org.metamechanists.quaptics.panels.info.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.tools.targetingwand.TargetingWand;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;

import java.util.Optional;

public class PointInfoPanelListener implements Listener {
    @EventHandler
    public static void interactEvent(@NotNull final PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        final ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (SlimefunItem.getByItem(heldItem) instanceof TargetingWand) {
            return;
        }

        final Optional<ConnectionPoint> point = new ConnectionPointId(clickedEntity.getUniqueId()).get();
        if (point.isEmpty()) {
            return;
        }

        point.get().togglePanelHidden();
    }
}
