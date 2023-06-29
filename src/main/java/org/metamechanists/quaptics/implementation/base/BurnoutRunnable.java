package org.metamechanists.quaptics.implementation.base;

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
    private final Location location;
    private final Location centerLocation;

    public BurnoutRunnable(Location location) {
        this.location = location;
        this.centerLocation = location.toCenterLocation();
    }

    @Override
    public void run() {
        final Block block = location.getBlock();
        if (!(BlockStorage.check(block) instanceof ConnectedBlock connectedBlock)) {
            return;
        }

        final DisplayGroup displayGroup = connectedBlock.getDisplayGroup(location.clone());
        final ConnectionGroup connectionGroup = connectedBlock.getGroup(location);
        if (displayGroup == null || connectionGroup == null) {
            return;
        }

        connectionGroup.getPoints().values().forEach(pointId -> {
            final ConnectionPoint point = pointId.get();
            if (point != null && point.hasLink() && RandomUtils.chance(50)) {
                point.getLink().setEnabled(false);
            }
        });

        for (int delay = 1; delay < 60; delay++) {
            final int ticks = delay;
            Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> {
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
                            .location(centerLocation)
                            .spawn();
                }
            }, delay);
        }

        Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> {
            // TODO Drop "Burnt Component" Item
            playSound(Sound.ENTITY_GENERIC_EXPLODE, 2, 1.2f);
            connectedBlock.burnout(block.getLocation());
        }, 60L);
    }

    public void playSound(Sound sound, float volume, float pitch) {
        location.getWorld().playSound(centerLocation, sound, volume, pitch);
    }
}
