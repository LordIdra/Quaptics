package org.metamechanists.quaptics.implementation.blocks.burnout;

import com.destroystokyo.paper.ParticleBuilder;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import lombok.Getter;
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
import org.metamechanists.quaptics.implementation.blocks.base.QuapticBlock;
import org.metamechanists.quaptics.utils.BlockStorageAPI;

import java.util.Optional;
import java.util.stream.IntStream;

public class BurnoutRunnable extends BukkitRunnable {
    private static final int BURN_TIME_TICKS = 60;
    private static final float FIZZLE_VOLUME = 0.1f;
    private static final float FIZZLE_PITCH = 0.8f;
    private static final int INITIAL_BRIGHTNESS = 13;
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
        final Optional<DisplayGroup> displayGroup = QuapticBlock.getDisplayGroup(location);
        final Optional<ConnectionGroup> connectionGroup = ConnectedBlock.getGroup(location);
        if (displayGroup.isEmpty() || connectionGroup.isEmpty() || !(BlockStorageAPI.check(block) instanceof final ConnectedBlock connectedBlock)) {
            return;
        }

        IntStream.range(1, BURN_TIME_TICKS).forEach(
                delay -> Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> tickDisplayGroup(displayGroup.get(), delay), delay));
        Bukkit.getScheduler().runTaskLater(Quaptics.getInstance(), () -> doFinalBurnout(connectedBlock), BURN_TIME_TICKS);
    }

    private void tickDisplayGroup(@NotNull final DisplayGroup displayGroup, final int ticks) {
        if (shouldStopEarly()) {
            return;
        }

        if (ticks % 4 == 0) {
            location.getWorld().playSound(centerLocation, Sound.BLOCK_LAVA_EXTINGUISH, FIZZLE_VOLUME, FIZZLE_PITCH);
        }

        if (ticks % 2 == 0) {
            doBurnoutAnimation(displayGroup);
        }
    }

    private void doBurnoutAnimation(@NotNull final DisplayGroup displayGroup) {
        displayGroup.getDisplays().values().forEach(display -> {
            final Brightness oldBrightness = display.getBrightness();
            final Brightness newBrightess = new Brightness(Optional.ofNullable(oldBrightness)
                    .map(brightness -> Math.max(0, brightness.getBlockLight() - 1))
                    .orElse(INITIAL_BRIGHTNESS), 0);
            display.setBrightness(newBrightess);
        });

        new ParticleBuilder(Particle.LAVA).location(centerLocation).spawn();
    }

    private void doFinalBurnout(final ConnectedBlock connectedBlock) {
        // TODO: Drop "Burnt Component" Item
        if (shouldStopEarly()) {
            return;
        }

        connectedBlock.burnout(location);
        BurnoutManager.removeBurnout(this);
    }

    private boolean shouldStopEarly() {
        return stopEarly;
    }

    public void stopEarly() {
        this.stopEarly = true;

        final Block block = location.getBlock();
        if (BlockStorageAPI.check(block) instanceof final ConnectedBlock connectedBlock) {
            connectedBlock.burnout(location);
        }
        BurnoutManager.removeBurnout(this);
    }
}
