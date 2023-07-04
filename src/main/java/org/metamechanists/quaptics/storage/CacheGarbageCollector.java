package org.metamechanists.quaptics.storage;

import org.bukkit.scheduler.BukkitRunnable;

public class CacheGarbageCollector extends BukkitRunnable {
    public static final int INTERVAL_TICKS = 20;

    @Override
    public void run() {
        QuapticCache.garbageCollect();
    }
}
