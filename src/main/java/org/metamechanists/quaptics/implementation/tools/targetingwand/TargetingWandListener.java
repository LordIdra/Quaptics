package org.metamechanists.quaptics.implementation.tools.targetingwand;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;

public class TargetingWandListener implements Listener {

    private static void useWand(final Player player, final @NotNull ConnectionPointId id, final TargetingWand wand, final ItemStack itemStack) {
        if (id.get().isEmpty()) {
            return;
        }

        wand.use(player, id, itemStack);
    }

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

        final ConnectionPointId pointId = new ConnectionPointId(clickedEntity.getUniqueId());

        if (SlimefunItem.getByItem(mainHandItem) instanceof final TargetingWand wand) {
            useWand(event.getPlayer(), pointId, wand, mainHandItem);
            return;
        }

        if (SlimefunItem.getByItem(offHandItem) instanceof final TargetingWand wand) {
            useWand(event.getPlayer(), pointId, wand, offHandItem);
        }
    }

    @EventHandler
    public static void scrollEvent(@NotNull final PlayerItemHeldEvent event) {
        final ItemStack heldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (SlimefunItem.getByItem(heldItem) instanceof TargetingWand) {
            TargetingWand.unsetSource(heldItem);
        }
    }
}
