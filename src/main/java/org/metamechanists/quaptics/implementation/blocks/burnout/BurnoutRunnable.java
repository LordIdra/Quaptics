package org.metamechanists.quaptics.implementation.blocks.burnout;

import com.destroystokyo.paper.ParticleBuilder;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.base.DisplayGroupTickerBlock;

import java.util.Optional;

public class BurnoutRunnable extends BukkitRunnable {
    private static final int BURN_TIME_TICKS = 60;
    private static final float FIZZLE_VOLUME = 0.1f;
    private static final float FIZZLE_PITCH = 0.8f;
    private static final int INITIAL_BRIGHTNESS = 14;
    @Getter
    private final Location location;
    @Getter
    private final Location centerLocation;
    private boolean stopEarly;

    public BurnoutRunnable(@NotNull final Location location) {
        super();
        this.location = location;
        this.centerLocation = location.toCenterLocation();
    }

    @Override
    public void run() {
        final Block block = location.getBlock();
        if (!(BlockStorage.check(block) instanceof final ConnectedBlock connectedBlock)) {
            return;
        }

        final Optional<DisplayGroup> displayGroup = DisplayGroupTickerBlock.getDisplayGroup(location);
        final Optional<ConnectionGroup> connectionGroup = ConnectedBlock.getGroup(location);
        if (displayGroup.isEmpty() || connectionGroup.isEmpty()) {
            return;
        }

        for (int delay = 1; delay < BURN_TIME_TICKS; delay++) {
            final int ticks = delay;
            Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> {
                if (shouldStopEarly()) {
                    return;
                }

                if (ticks % 4 == 0) {
                    location.getWorld().playSound(centerLocation, Sound.BLOCK_LAVA_EXTINGUISH, FIZZLE_VOLUME, FIZZLE_PITCH);
                }

                if (ticks % 2 == 0) {
                    displayGroup.get().getDisplays().values().forEach(display -> {
                        final Brightness brightness = display.getBrightness();
                        display.setBrightness(new Brightness(brightness == null ? INITIAL_BRIGHTNESS : Math.max(0, brightness.getBlockLight() - 1), 0));
                    });

                    new ParticleBuilder(Particle.LAVA)
                            .location(centerLocation)
                            .spawn();
                }
            }, delay);
        }

        Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> {
            // TODO: Drop "Burnt Component" Item
            if (shouldStopEarly()) {
                return;
            }

            connectedBlock.burnout(location);
            BurnoutManager.removeBurnout(this);
        }, BURN_TIME_TICKS);
    }

    private boolean shouldStopEarly() {
        return stopEarly;
    }

    public void stopEarly() {
        this.stopEarly = true;

        final Block block = location.getBlock();
        if (BlockStorage.check(block) instanceof final ConnectedBlock connectedBlock) {
            connectedBlock.burnout(location);
        }
        BurnoutManager.removeBurnout(this);
    }
}
