package org.metamechanists.death_lasers.connections;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockUpdateScheduler {
    // DANGER - this class is responsible for ensuring no update loops occur, which could crash the server and then make restarting it apallingly difficult
    // Think VERY carefully before modifying

    private static Queue<ConnectionGroup> newGroupsToTick = new ConcurrentLinkedQueue<>();

    private static void tickGroup(ConnectionGroup group) {
        // If group no longer exists, don't tick it
        if (!ConnectionPointStorage.hasGroup(group.getId())) {
            return;
        }

        group.getBlock().onInputLinkUpdated(group);
    }

    public static void scheduleUpdate(ConnectionGroup group) {
        newGroupsToTick.add(group);
    }

    public static void tick() {
        final Queue<ConnectionGroup> oldGroupsToTick = newGroupsToTick;
        newGroupsToTick = new ConcurrentLinkedQueue<>();

        while (!oldGroupsToTick.isEmpty()) {
            tickGroup(oldGroupsToTick.remove());
        }
    }
}
