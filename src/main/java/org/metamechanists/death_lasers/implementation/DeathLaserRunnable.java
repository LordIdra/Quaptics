package org.metamechanists.death_lasers.implementation;

import org.bukkit.scheduler.BukkitRunnable;

public class DeathLaserRunnable extends BukkitRunnable {

    @Override
    public void run() {
        LaserDisplayStorage.update();
    }
}
