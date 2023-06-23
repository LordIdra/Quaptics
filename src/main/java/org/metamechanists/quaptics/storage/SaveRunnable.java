package org.metamechanists.quaptics.storage;

import org.bukkit.scheduler.BukkitRunnable;

public class SaveRunnable extends BukkitRunnable {
    public static final int INTERVAL_TICKS = 6000;
    public void run() {
        Storage.save();
    }
}
