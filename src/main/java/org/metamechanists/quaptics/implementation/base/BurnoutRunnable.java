package org.metamechanists.quaptics.implementation.base;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class BurnoutRunnable extends BukkitRunnable {
    private final Location location;
    private int ticks = 1;

    public BurnoutRunnable(Location location) {
        this.location = location;
    }

    @Override
    public void run() {
        final Block block = location.getBlock();
        if (!(BlockStorage.check(block) instanceof ConnectedBlock connectedBlock)) {
            return;
        }

        if (ticks == 60) {
            connectedBlock.burnout(block.getLocation());
            return;
        }

        location.getWorld().spawnParticle(Particle.LAVA, location, 1);
        ticks++;

        run();
    }
}
