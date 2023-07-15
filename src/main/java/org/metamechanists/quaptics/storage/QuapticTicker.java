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
    public static final int INTERVAL_TICKS = 2;
    public static final int INTERVAL_TICKS_2 = 2;
    public static final int INTERVAL_TICKS_6 = 6;
    public static final int INTERVAL_TICKS_10 = 10;
    public static final int INTERVAL_TICKS_22 = 22;
    public static final int INTERVAL_TICKS_102 = 102;

    @Override
    public void run() {
        time += INTERVAL_TICKS;
        for (final ConnectionGroupId groupId : QuapticStorage.getTickingGroupIds()) {
            if (groupId.get().isEmpty()) {
                continue;
            }

            final ConnectionGroup group = groupId.get().get();
            try {
                group.tick2();

                if (time % INTERVAL_TICKS_6 == 0) {
                    group.tick6();
                }

                if (time % INTERVAL_TICKS_22 == 0) {
                    group.tick22();
                }

                if (time % INTERVAL_TICKS_102 == 0) {
                    group.tick102();
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