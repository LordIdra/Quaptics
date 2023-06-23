package org.metamechanists.quaptics;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.quaptics.connections.ConnectionPointStorage;
import org.metamechanists.quaptics.connections.BlockUpdateScheduler;
import org.metamechanists.quaptics.beams.DeprecatedTickerStorage;

public class LaserTicker extends BukkitRunnable {
    public static final int INTERVAl_TICKS = 1;

    @Override
    public void run() {
        ConnectionPointStorage.tick();
        DeprecatedTickerStorage.tick();
        BlockUpdateScheduler.tick();
    }
}
