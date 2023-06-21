package org.metamechanists.death_lasers.beams.ticker.ticker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class DirectSinglePulseTicker implements LaserBlockDisplayTicker {
    // Not quite 1 to prevent Z-fighting with connection points
    private final BlockDisplay display;

    public DirectSinglePulseTicker(Material material, Location source, Location target, float size) {
        final float scale = size * 0.095F;
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scaleVector = new Vector3f(size, size, (float)(source.distance(target)));
        this.display = DisplayUtils.spawnBlockDisplay(midpoint, material, DisplayUtils.faceTargetTransformation(midpoint, target, scaleVector));
        display.setBrightness(new Display.Brightness(15, 15));
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

