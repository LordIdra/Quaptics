package org.metamechanists.quaptics.implementation.base;

import com.destroystokyo.paper.ParticleBuilder;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.connections.ConnectionGroup;

public class BurnoutRunnable extends BukkitRunnable {
    @Getter
    private final Location location;
    @Getter
    private final Location centerLocation;
    private boolean stopEarly = false;

    public BurnoutRunnable(@NotNull Location location) {
        this.location = location;
        this.centerLocation = location.toCenterLocation();
    }

    @Override
    public void run() {
        final Block block = this.location.getBlock();
        if (!(BlockStorage.check(block) instanceof ConnectedBlock connectedBlock)) {
            return;
        }

        final DisplayGroup displayGroup = connectedBlock.getDisplayGroup(this.location);
        final ConnectionGroup connectionGroup = connectedBlock.getGroup(this.location);
        if (displayGroup == null || connectionGroup == null) {
            return;
        }

        for (int delay = 1; delay < 60; delay++) {
            final int ticks = delay;
            Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> {
                if (shouldStopEarly()) {
                    return;
                }

                if (ticks % 4 == 0) {
                    location.getWorld().playSound(centerLocation, Sound.BLOCK_LAVA_EXTINGUISH, 0.1f, 0.8f);
                }

                if (ticks % 2 == 0) {
                    displayGroup.getDisplays().values().forEach(display -> {
                        final Display.Brightness brightness = display.getBrightness();
                        display.setBrightness(new Display.Brightness(
                                brightness == null ? 14 : Math.max(0, brightness.getBlockLight() - 1),
                                brightness == null ? 14 : Math.max(0, brightness.getSkyLight() - 1)));
                    });

                    new ParticleBuilder(Particle.LAVA)
                            .location(this.centerLocation)
                            .spawn();
                }
            }, delay);
        }

        Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> {
            // TODO: Drop "Burnt Component" Item
            if (shouldStopEarly()) {
                return;
            }

            connectedBlock.burnout(this.location);
            BurnoutManager.removeBurnout(this);
        }, 60L);
    }

    public boolean shouldStopEarly() {
        return this.stopEarly;
    }

    public void stopEarly() {
        this.stopEarly = true;

        final Block block = this.location.getBlock();
        if (BlockStorage.check(block) instanceof ConnectedBlock connectedBlock) {
            connectedBlock.burnout(this.location);
        }
        BurnoutManager.removeBurnout(this);
    }
}
