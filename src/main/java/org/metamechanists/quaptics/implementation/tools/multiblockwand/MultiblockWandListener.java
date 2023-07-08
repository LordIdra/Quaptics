package org.metamechanists.quaptics.implementation.tools.multiblockwand;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.Language;

public class MultiblockWandListener implements Listener {
    @EventHandler
    public static void interactEvent(@NotNull final PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        final ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (!(SlimefunItem.getByItem(heldItem) instanceof MultiblockWand)) {
            return;
        }

        final PersistentDataTraverser traverser = new PersistentDataTraverser(clickedEntity.getUniqueId());
        if (traverser.getString("blockName") == null) {
            return;
        }

        Language.sendLanguageMessage(event.getPlayer(), "multiblock.block-name", traverser.getString("blockName"));
    }

    @EventHandler
    public static void scrollEvent(@NotNull final PlayerItemHeldEvent event) {
        final ItemStack heldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (SlimefunItem.getByItem(heldItem) instanceof MultiblockWand) {
            MultiblockWand.removeProjection(heldItem);
        }
    }
}
