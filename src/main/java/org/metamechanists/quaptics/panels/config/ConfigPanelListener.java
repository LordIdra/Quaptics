package org.metamechanists.quaptics.panels.config;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.attachments.ConfigPanelBlock;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;

public class ConfigPanelListener implements Listener {
    @EventHandler
    public static void interactEvent(@NotNull final PlayerInteractEntityEvent event) {
        final Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof Interaction)) {
            return;
        }

        final PersistentDataTraverser traverser = new PersistentDataTraverser(clickedEntity.getUniqueId());
        final String type = traverser.getString("type");
        if (type == null) {
            return;
        }

        final String name = traverser.getString("name");
        if (name == null) {
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

        if (!(group.get().getBlock() instanceof final ConfigPanelBlock block)) {
            return;
        }

        block.interact(group.get(), name, type);
    }
}
