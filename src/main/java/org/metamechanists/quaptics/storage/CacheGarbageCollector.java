package org.metamechanists.quaptics.storage;

import org.bukkit.scheduler.BukkitRunnable;

public class CacheGarbageCollector extends BukkitRunnable {
    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static final int INTERVAL_TICKS = 601;

    @Override
    public void run() {
        QuapticCache.garbageCollect();
    }
}
