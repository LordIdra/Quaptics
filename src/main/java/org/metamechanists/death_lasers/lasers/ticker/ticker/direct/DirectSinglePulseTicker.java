package org.metamechanists.death_lasers.lasers.ticker.ticker.direct;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class DirectSinglePulseTicker implements LaserBlockDisplayTicker {
    private static final float SCALE = 0.1F;
    private final BlockDisplay display;

    public DirectSinglePulseTicker(Material material, Location source, Location target) {
        final Location midpoint = source.clone().add(target).multiply(0.5);
        this.display = DisplayUtils.spawnBlockDisplay(source, material, DisplayUtils.faceTargetTransformation(midpoint, target, SCALE));
    }

    @Override
    public void tick() {}

    @Override
    public void remove() {
        display.remove();
    }

    @Override
    public boolean expired() {
        return true;
    }
}

