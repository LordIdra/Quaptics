package org.metamechanists.quaptics.storage;

import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask extends BukkitRunnable {
    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static final int INTERVAL_TICKS = 6000;

    @Override
    public void run() {
        QuapticStorage.save();
    }
}
