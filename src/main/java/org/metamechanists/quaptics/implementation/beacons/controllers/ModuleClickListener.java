package org.metamechanists.quaptics.implementation.beacons.controllers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;


public class ModuleClickListener implements Listener {
    @EventHandler
    public static void interactEvent(@NotNull final PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        final PersistentDataTraverser traverser = new PersistentDataTraverser(clickedEntity.getUniqueId());
        final String slot = traverser.getString("slot");
        if (slot == null) {
            return;
        }

        final ConnectionGroupId groupId = traverser.getConnectionGroupId("groupId");
        if (groupId == null) {
            return;
        }

        final Optional<ConnectionGroup> group = groupId.get();
        if (group.isEmpty()) {
            return;
        }

        if (!(group.get().getBlock() instanceof final BeaconController block)) {
            return;
        }

        block.interact(event.getPlayer(), group.get(), slot);
    }
}
