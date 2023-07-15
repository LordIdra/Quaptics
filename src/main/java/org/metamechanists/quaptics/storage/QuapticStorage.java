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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class QuapticStorage implements Listener {
    @Getter
    private static final Set<ConnectionGroupId> groupIDs = new HashSet<>();

    public static void addGroup(final ConnectionGroupId groupId) {
        groupIDs.add(groupId);
    }
    public static void removeGroup(final ConnectionGroupId groupId) {
        groupIDs.remove(groupId);
    }

    private static Collection<ConnectionGroupId> getConnectionGroupIds(final @NotNull Collection<Entity> entities) {
        return entities.stream()
                .filter(entity -> entity instanceof Interaction)
                .map(Interaction.class::cast)
                .map(interaction -> new ConnectionGroupId(interaction.getUniqueId()))
                .filter(connectionGroupId -> connectionGroupId.get().isPresent())
                .toList();
    }

    @EventHandler
    public static void onEntityLoad(final @NotNull EntitiesLoadEvent event) {
        groupIDs.addAll(getConnectionGroupIds(event.getEntities()));
    }
    @EventHandler
    public static void onEntityUnload(final @NotNull EntitiesUnloadEvent event) {
        groupIDs.removeAll(getConnectionGroupIds(event.getEntities()));
    }
}
