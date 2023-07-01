package org.metamechanists.quaptics.implementation.panels;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

public class CapacitorPanelListener implements Listener {
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

        final ConnectionGroup group = new ConnectionGroupId(clickedEntity.getUniqueId()).get();
        if (group == null) {
            return;
        }

        if (!(group.getBlock() instanceof Capacitor)) {
            return;
        }

        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        final PanelId id = Capacitor.getPanelId(location);
        if (id == null) {
            return;
        }

        final CapacitorPanel panel = new CapacitorPanel(id, group.getId());
        panel.toggleHidden();
    }
}
