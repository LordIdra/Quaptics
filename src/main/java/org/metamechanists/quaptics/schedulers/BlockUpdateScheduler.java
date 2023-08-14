package org.metamechanists.quaptics.schedulers;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class BlockUpdateScheduler {
    // DANGER - this class is responsible for ensuring no update loops occur, which could crash the server and then make restarting it appallingly difficult

    private static Queue<ConnectionGroupId> newGroupsToTick = new ConcurrentLinkedQueue<>();

    private BlockUpdateScheduler() {}

    private static void tickGroup(final @NotNull ConnectionGroupId groupId) {
        final Optional<ConnectionGroup> connectionGroup = groupId.get();
        if (connectionGroup.isEmpty()) {
            return;
        }

        final Optional<Location> location = connectionGroup.get().getLocation();
        if (location.isEmpty()) {
            return;
        }

        groupId.get().ifPresent(group -> group.getBlock().onInputLinkUpdated(group, location.get()));
    }

    public static void scheduleUpdate(final ConnectionGroupId groupId) {
        newGroupsToTick.add(groupId);
    }

    public static void tick() {
        final Queue<ConnectionGroupId> oldGroupsToTick = newGroupsToTick;
        newGroupsToTick = new ConcurrentLinkedQueue<>();
        while (!oldGroupsToTick.isEmpty()) {
            tickGroup(oldGroupsToTick.remove());
        }
    }
}
