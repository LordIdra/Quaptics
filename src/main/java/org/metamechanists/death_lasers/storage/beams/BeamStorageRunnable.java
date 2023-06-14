package org.metamechanists.death_lasers.storage.beams;

import org.bukkit.scheduler.BukkitRunnable;

public class BeamStorageRunnable extends BukkitRunnable {

    @Override
    public void run() {
        BeamStorage.tick();
    }
}
