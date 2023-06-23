package org.metamechanists.death_lasers.connections;

import org.metamechanists.death_lasers.utils.id.ConnectionGroupID;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockUpdateScheduler {
    // DANGER - this class is responsible for ensuring no update loops occur, which could crash the server and then make restarting it apallingly difficult

    private static Queue<ConnectionGroupID> newGroupsToTick = new ConcurrentLinkedQueue<>();

    private static void tickGroup(ConnectionGroupID groupID) {
        // If group no longer exists, don't tick it
        if (!ConnectionPointStorage.hasGroup(groupID)) {
            return;
        }

        final ConnectionGroup group = ConnectionPointStorage.getGroup(groupID);
        group.getBlock().onInputLinkUpdated(group);
    }

    public static void scheduleUpdate(ConnectionGroupID groupID) {
        newGroupsToTick.add(groupID);
    }

    public static void tick() {
        final Queue<ConnectionGroupID> oldGroupsToTick = newGroupsToTick;
        newGroupsToTick = new ConcurrentLinkedQueue<>();
        while (!oldGroupsToTick.isEmpty()) {
            tickGroup(oldGroupsToTick.remove());
        }
    }
}
