package org.metamechanists.death_lasers.lasers.ticker.ticker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class DirectSinglePulseTicker implements LaserBlockDisplayTicker {
    // Not quite 1 to prevent Z-fighting with connection points
    private static final float SCALE = 0.095F;
    private final BlockDisplay display;

    public DirectSinglePulseTicker(Material material, Location source, Location target) {
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scale = new Vector3f(SCALE, SCALE, (float)(source.distance(target)));
        this.display = DisplayUtils.spawnBlockDisplay(midpoint, material,
                DisplayUtils.faceTargetTransformation(midpoint, target, scale));
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

