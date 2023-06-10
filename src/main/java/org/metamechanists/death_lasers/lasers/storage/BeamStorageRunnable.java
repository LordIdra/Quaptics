package org.metamechanists.death_lasers.lasers.storage;

import org.bukkit.scheduler.BukkitRunnable;

public class BeamStorageRunnable extends BukkitRunnable {

    @Override
    public void run() {
        BeamStorage.tick();
    }
}
