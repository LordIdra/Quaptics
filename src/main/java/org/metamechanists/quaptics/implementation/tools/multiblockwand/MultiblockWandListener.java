package org.metamechanists.quaptics.implementation.tools.multiblockwand;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MultiblockWandListener implements Listener {
    @EventHandler
    public static void interactEvent(@NotNull final PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        if (!Slimefun.getProtectionManager().hasPermission(event.getPlayer(), clickedEntity.getLocation(), io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction.INTERACT_ENTITY)) {
            return;
        }

        final ItemStack mainHandItem = event.getPlayer().getInventory().getItemInMainHand();
        final ItemStack offHandItem = event.getPlayer().getInventory().getItemInOffHand();
        if ((SlimefunItem.getByItem(mainHandItem) instanceof MultiblockWand) || (SlimefunItem.getByItem(offHandItem) instanceof MultiblockWand)) {
            MultiblockWand.tellPlayerBlock(clickedEntity, event.getPlayer());
        }
    }

    @EventHandler
    public static void scrollEvent(@NotNull final PlayerItemHeldEvent event) {
        final ItemStack heldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (SlimefunItem.getByItem(heldItem) instanceof MultiblockWand) {
            MultiblockWand.removeProjection(heldItem);
        }
    }
}
