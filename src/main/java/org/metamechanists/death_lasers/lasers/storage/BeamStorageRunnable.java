package org.metamechanists.death_lasers.lasers.storage;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.death_lasers.lasers.storage.BeamStorage;

public class BeamStorageRunnable extends BukkitRunnable {

    @Override
    public void run() {
        BeamStorage.tick();
    }
}
