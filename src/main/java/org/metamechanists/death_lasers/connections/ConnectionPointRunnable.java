package org.metamechanists.death_lasers.connections;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.death_lasers.lasers.DeprecatedBeams;

public class ConnectionPointRunnable extends BukkitRunnable {

    @Override
    public void run() {
        DeprecatedBeams.tick();
    }
}
