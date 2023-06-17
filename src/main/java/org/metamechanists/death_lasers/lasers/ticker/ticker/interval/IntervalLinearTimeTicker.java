package org.metamechanists.death_lasers.lasers.ticker.ticker.interval;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class IntervalLinearTimeTicker implements LaserBlockDisplayTicker {
    // Not quite 1 to prevent Z-fighting with connection points
    private static final Vector3f SCALE = new Vector3f(0.095F, 0.095F, 0.095F);
    private final int lifespanTicks;
    private final Vector velocity;
    private final BlockDisplay display;
    private int ageTicks = 0;

    public IntervalLinearTimeTicker(Material material, Location source, Location target, int lifespanTicks) {
        this.lifespanTicks = lifespanTicks;
        this.velocity = DisplayUtils.getDisplacement(source, target).multiply(1.0/lifespanTicks);
        this.display = DisplayUtils.spawnBlockDisplay(source, material, DisplayUtils.faceTargetTransformation(source, target, SCALE));
    }

    @Override
    public void tick() {
        display.teleport(display.getLocation().add(velocity));
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

