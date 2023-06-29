package org.metamechanists.quaptics.implementation.base;

import com.destroystokyo.paper.ParticleBuilder;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.quaptics.Quaptics;

public class BurnoutRunnable extends BukkitRunnable {
    private final Location location;

    public BurnoutRunnable(Location location) {
        this.location = location;
    }

    @Override
    public void run() {
        final Block block = location.getBlock();
        if (!(BlockStorage.check(block) instanceof ConnectedBlock connectedBlock)) {
            return;
        }

        for (int delay = 1; delay < 60; delay++) {
            final int ticks = delay;
            Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> {
                final DisplayGroup group = connectedBlock.getDisplayGroup(location.clone());
                if (group != null && ticks % 4 == 0) {
                    group.getDisplays().values().forEach(display -> {
                        final Display.Brightness brightness = display.getBrightness();
                        display.setBrightness(new Display.Brightness(
                                brightness == null ? 14 : Math.max(0, brightness.getBlockLight() - 1),
                                brightness == null ? 14 : Math.max(0, brightness.getSkyLight() - 1)));
                    });

                    new ParticleBuilder(Particle.LAVA)
                            .location(location.clone().add(ConnectedBlock.RELATIVE_CENTER))
                            .spawn();
                }
            }, delay);
        }

        Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> connectedBlock.burnout(block.getLocation()), 60L);
    }
}
