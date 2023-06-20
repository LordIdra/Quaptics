package org.metamechanists.death_lasers;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.BlockUpdateScheduler;
import org.metamechanists.death_lasers.beams.DeprecatedBeamStorage;

public class LaserTicker extends BukkitRunnable {

    @Override
    public void run() {
        ConnectionPointStorage.tick();
        DeprecatedBeamStorage.tick();
        BlockUpdateScheduler.tick();
    }
}
