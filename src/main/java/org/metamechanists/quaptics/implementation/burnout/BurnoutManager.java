package org.metamechanists.quaptics.implementation.burnout;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collection;
import java.util.HashSet;

public class BurnoutManager implements Listener {
    private static final Collection<BurnoutRunnable> burnouts = new HashSet<>();

    public static void addBurnout(final BurnoutRunnable runnable) {
        burnouts.add(runnable);
        runnable.run();
    }

    public static void removeBurnout(final BurnoutRunnable runnable) {
        burnouts.remove(runnable);
    }

    public static void stopBurnouts() {
        burnouts.forEach(BurnoutRunnable::stopEarly);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public static void onBlockBreak(final BlockBreakEvent event) {
        for (final BurnoutRunnable runnable : burnouts) {
            if (event.getBlock().equals(runnable.getLocation().getBlock())) {
                event.setCancelled(true);
                runnable.stopEarly();
                return;
            }
        }
    }
}
