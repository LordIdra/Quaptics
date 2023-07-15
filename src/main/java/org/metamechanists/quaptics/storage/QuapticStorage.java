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
import java.util.stream.Collectors;


public class QuapticStorage implements Listener {
    @Getter
    private static final Set<ConnectionGroupId> tickingGroupIds = new HashSet<>();

    private static boolean isTickingGroup(final Interaction interaction) {
        return new PersistentDataTraverser(interaction).getBoolean("isTicker");
    }

    public static void addGroup(final ConnectionGroupId groupId) {
        tickingGroupIds.add(groupId);
    }
    public static void removeGroup(final ConnectionGroupId groupId) {
        tickingGroupIds.remove(groupId);
    }

    private static @NotNull Collection<ConnectionGroupId> getTickingGroupIds(final @NotNull Collection<Entity> entities) {
        return entities.stream()
                .filter(entity -> entity instanceof Interaction)
                .map(entity -> (Interaction) entity)
                .filter(QuapticStorage::isTickingGroup)
                .map(interaction -> new ConnectionGroupId(interaction.getUniqueId()))
                .collect(Collectors.toList());
    }

    @EventHandler
    public static void onEntityLoad(final @NotNull EntitiesLoadEvent event) {
        tickingGroupIds.addAll(getTickingGroupIds(event.getEntities()));
    }
    @EventHandler
    public static void onEntityUnload(final @NotNull EntitiesUnloadEvent event) {
        tickingGroupIds.removeAll(getTickingGroupIds(event.getEntities()));
    }
}
