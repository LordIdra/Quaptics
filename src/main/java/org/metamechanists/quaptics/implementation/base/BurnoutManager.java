package org.metamechanists.quaptics.implementation.base;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;

public class BurnoutManager implements Listener {
    private static final Set<BurnoutRunnable> burnouts = new HashSet<>();

    public static void addBurnout(BurnoutRunnable runnable) {
        burnouts.add(runnable);
        runnable.run();
    }

    public static void removeBurnout(BurnoutRunnable runnable) {
        burnouts.remove(runnable);
    }

    public static void stopBurnouts() {
        burnouts.forEach(BurnoutRunnable::stopEarly);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        for (BurnoutRunnable runnable : burnouts) {
            if (event.getBlock().equals(runnable.getLocation().getBlock())) {
                event.setCancelled(true);
                runnable.stopEarly();
                return;
            }
        }
    }
}
