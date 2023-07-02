package org.metamechanists.quaptics.implementation.tools.targetingwand;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;

public class TargetingWandListener implements Listener {

    @EventHandler
    public void interactEvent(@NotNull final PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        final ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (!(SlimefunItem.getByItem(heldItem) instanceof final TargetingWand wand)) {
            return;
        }

        final ConnectionPointId pointId = new ConnectionPointId(clickedEntity.getUniqueId());
        if (pointId.get().isEmpty()) {
            return;
        }

        wand.use(event.getPlayer(), pointId, heldItem);
    }

    @EventHandler
    public void scrollEvent(@NotNull final PlayerItemHeldEvent event) {
        final ItemStack heldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (SlimefunItem.getByItem(heldItem) instanceof TargetingWand) {
            TargetingWand.unsetSource(heldItem);
        }
    }
}
