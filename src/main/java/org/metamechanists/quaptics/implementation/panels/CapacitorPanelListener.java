package org.metamechanists.quaptics.implementation.panels;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
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
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.PanelID;

public class CapacitorPanelListener implements Listener {
    @EventHandler
    public void interactEvent(@NotNull PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        final ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (SlimefunItem.getByItem(heldItem) instanceof TargetingWand) {
            return;
        }

        final ConnectionGroup group = new ConnectionGroupID(clickedEntity.getUniqueId()).get();
        if (group == null) {
            return;
        }

        if (!(group.getBlock() instanceof Capacitor capacitor)) {
            return;
        }

        final PanelID ID = capacitor.getPanelID(group.getLocation());
        if (ID == null) {
            return;
        }

        final CapacitorPanel panel = new CapacitorPanel(ID, group.getID());
        panel.toggleHidden();
    }
}
