package org.metamechanists.quaptics.schedulers;

import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class BlockUpdateScheduler {
    // DANGER - this class is responsible for ensuring no update loops occur, which could crash the server and then make restarting it appallingly difficult

    private static Queue<ConnectionGroupId> newGroupsToTick = new ConcurrentLinkedQueue<>();

    private BlockUpdateScheduler() {}

    private static void tickGroup(final @NotNull ConnectionGroupId groupId) {
        groupId.get().ifPresent(group -> group.getBlock().onInputLinkUpdated(group));
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
