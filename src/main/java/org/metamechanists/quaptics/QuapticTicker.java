package org.metamechanists.quaptics;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.quaptics.beams.DeprecatedTickerStorage;
import org.metamechanists.quaptics.connections.BlockUpdateScheduler;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.storage.QuapticStorage;

public class QuapticTicker extends BukkitRunnable {
    public static final int INTERVAl_TICKS = 1;

    @Override
    public void run() {
        QuapticStorage.getLoadedGroups().forEach(ID -> ID.get().tick());
        DeprecatedTickerStorage.tick();
        BlockUpdateScheduler.tick();
    }
}
