package org.metamechanists.death_lasers.connections;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockUpdateScheduler {
    // DANGER - this class is responsible for ensuring no update loops occur, which would crash the server and then make restarting it apallingly difficult
    // Think VERY carefully before modifying

    private static final Queue<ConnectionGroup> groupsToUpdate = new ConcurrentLinkedQueue<>();
    private static final Set<ConnectionGroup> groupsUpdatedThisTick = new HashSet<>();

    public static void scheduleUpdate(ConnectionGroup group) {
        if (!groupsUpdatedThisTick.contains(group)) {
            groupsUpdatedThisTick.add(group);
            group.getBlock().onLinkUpdated(group);
        } else {
            groupsToUpdate.add(group);
        }
    }

    public static void tick() {
        groupsToUpdate.forEach(group -> {
            groupsUpdatedThisTick.add(group);
            group.getBlock().onLinkUpdated(group);
        });

        groupsToUpdate.clear();
        groupsUpdatedThisTick.clear();
    }
}
