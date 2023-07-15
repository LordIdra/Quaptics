package org.metamechanists.quaptics.storage;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.schedulers.BlockUpdateScheduler;
import org.metamechanists.quaptics.schedulers.PointPanelUpdateScheduler;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

public class QuapticTicker extends BukkitRunnable {
    private static long time;

    public static final int TICKS_PER_SECOND = 20;
    public static final int INTERVAL_TICKS = 1;
    public static final int INTERVAL_TICKS_2 = 2;
    public static final int INTERVAL_TICKS_5 = 5;
    public static final int INTERVAL_TICKS_10 = 10;
    public static final int INTERVAL_TICKS_21 = 21;

    @Override
    public void run() {
        time++;
        for (final ConnectionGroupId groupId : QuapticStorage.getGroupIDs()) {
            if (groupId.get().isEmpty()) {
                continue;
            }

            final ConnectionGroup group = groupId.get().get();

            try {
                if (time % INTERVAL_TICKS_2 == 0) {
                    group.tick2();
                }
                if (time % INTERVAL_TICKS_5 == 0) {
                    group.tick5();
                }
                if (time % INTERVAL_TICKS_21 == 0) {
                    group.tick21();
                }
            } catch (final RuntimeException exception) {
                QuapticStorage.removeGroup(group.getId());
                exception.printStackTrace();
            }
        }

        DeprecatedBeamStorage.tick();
        BlockUpdateScheduler.tick();
        PointPanelUpdateScheduler.tick();
    }
}