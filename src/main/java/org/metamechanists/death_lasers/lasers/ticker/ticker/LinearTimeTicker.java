package org.metamechanists.death_lasers.lasers.ticker.ticker;

import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.DEATH_LASERS;

public class LinearTimeTicker implements LaserBlockDisplayTicker {
    private final BlockDisplay display;
    private final Vector velocity;
    private final int lifespanTicks;
    private int ageTicks = 0;

    public LinearTimeTicker(BlockDisplay display, Location source, Location target, int lifespanTicks) {
        this.display = display;
        this.lifespanTicks = lifespanTicks;
        DEATH_LASERS.getInstance().getLogger().severe("hi mum" + source.toString());
        DEATH_LASERS.getInstance().getLogger().severe("hi mum" + target.toString());
        DEATH_LASERS.getInstance().getLogger().severe("hi mum" + target.toVector().subtract(source.toVector()));
        velocity = target.toVector()
                .subtract(source.toVector())
                .multiply(1.0/lifespanTicks);
    }


    @Override
    public void tick() {
        DEATH_LASERS.getInstance().getLogger().severe(velocity.toString());
        display.setVelocity(velocity);
        ageTicks++;
    }

    @Override
    public void remove() {
        display.remove();
    }

    @Override
    public boolean expired() {
        return ageTicks >= lifespanTicks;
    }
}

