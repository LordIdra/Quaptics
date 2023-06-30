package org.metamechanists.quaptics.implementation.base;

import lombok.Getter;

import com.destroystokyo.paper.ParticleBuilder;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.scheduler.BukkitRunnable;

import org.metamechanists.metalib.utils.RandomUtils;

import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;

public class BurnoutRunnable extends BukkitRunnable {
    @Getter
    private final Location location;
    @Getter
    private final Location centerLocation;
    private boolean stopEarly = false;

    public BurnoutRunnable(Location location) {
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
                if (this.stopEarly) {
                    return;
                }

                if (ticks % 2 + RandomUtils.randomInteger(1, 5) == 0) {
                    connectionGroup.getPoints().values().forEach(pointId -> {
                        final ConnectionPoint point = pointId.get();
                        if (point != null && point.hasLink()) {
                            point.getLink().setEnabled(!point.getLink().isEnabled());
                        }
                    });
                }

                if (ticks % 4 == 0) {
                    playSound(Sound.BLOCK_LAVA_EXTINGUISH, 0.1f, 0.8f);
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
            if (this.stopEarly) {
                return;
            }

            playSound(Sound.ENTITY_GENERIC_EXPLODE, 2, 1.2f);
            connectedBlock.burnout(this.location);
        }, 60L);
    }

    public void playSound(Sound sound, float volume, float pitch) {
        location.getWorld().playSound(centerLocation, sound, volume, pitch);
    }

    public void stopEarly() {
        this.stopEarly = true;

        final Block block = this.location.getBlock();
        if (BlockStorage.check(block) instanceof ConnectedBlock connectedBlock) {
            connectedBlock.burnout(this.location);
        }
    }
}
