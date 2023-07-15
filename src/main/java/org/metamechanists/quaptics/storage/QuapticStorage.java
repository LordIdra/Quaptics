package org.metamechanists.quaptics.storage;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuapticStorage implements Listener {
    @Getter
    private static final Set<ConnectionGroupId> groupIDs = new HashSet<>();

    private static boolean isGroup(final Interaction interaction) {
        return new PersistentDataTraverser(interaction).getString("blockId") != null;
    }

    public static void addGroup(final ConnectionGroupId groupId) {
        groupIDs.add(groupId);
    }
    public static void removeGroup(final ConnectionGroupId groupId) {
        groupIDs.remove(groupId);
    }

    private static @NotNull Collection<ConnectionGroupId> getConnectionGroupIds(final @NotNull Collection<Entity> entities) {
        List<ConnectionGroupId> list = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Interaction) {
                Interaction interaction = (Interaction) entity;
                ConnectionGroupId groupId = new ConnectionGroupId(interaction.getUniqueId());
                if (isGroup(interaction)) {
                    list.add(groupId);
                }
            }
        }
        return list;
    }

    @EventHandler
    public static void onEntityLoad(final @NotNull EntitiesLoadEvent event) {
        if (event.getEntities().isEmpty()) {
            return;
        }
        groupIDs.addAll(getConnectionGroupIds(event.getEntities()));
    }
    @EventHandler
    public static void onEntityUnload(final @NotNull EntitiesUnloadEvent event) {
        if (event.getEntities().isEmpty()) {
            return;
        }
        groupIDs.removeAll(getConnectionGroupIds(event.getEntities()));
    }
}
