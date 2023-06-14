package org.metamechanists.death_lasers.lasers;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.death_lasers.lasers.BeamStorage;

public class BeamStorageRunnable extends BukkitRunnable {

    @Override
    public void run() {
        BeamStorage.tick();
    }
}
