package org.metamechanists.quaptics.connections;

import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockUpdateScheduler {
    // DANGER - this class is responsible for ensuring no update loops occur, which could crash the server and then make restarting it appallingly difficult

    private static Queue<ConnectionGroupId> newGroupsToTick = new ConcurrentLinkedQueue<>();

    private static void tickGroup(ConnectionGroupId groupId) {
        final ConnectionGroup group = groupId.get();
        if (group == null) {
            return;
        }

        group.getBlock().onInputLinkUpdated(group);
    }

    public static void scheduleUpdate(ConnectionGroupId groupId) {
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
