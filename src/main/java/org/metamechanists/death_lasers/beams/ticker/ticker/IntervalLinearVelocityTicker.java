package org.metamechanists.death_lasers.beams.ticker.ticker;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.DisplayUtils;
import org.metamechanists.death_lasers.utils.id.BlockDisplayID;

public class IntervalLinearVelocityTicker implements LaserBlockDisplayTicker {
    // Not quite 1 to prevent Z-fighting with connection points
    private static final Vector3f SCALE = new Vector3f(0.095F, 0.095F, 0.20F);
    private final Vector velocity;
    private final BlockDisplayID displayID;
    private final int lifespanTicks;
    private int ageTicks = 0;

    public IntervalLinearVelocityTicker(Material material, Location source, Location target, double speed) {
        this.velocity = DisplayUtils.getDisplacement(source, target).normalize().multiply(speed);
        lifespanTicks = (int)(DisplayUtils.getDisplacement(source, target).length() / speed) + 1;
        final BlockDisplay display = DisplayUtils.spawnBlockDisplay(source, material, DisplayUtils.faceTargetTransformation(source, target, SCALE));
        // TODO make a proper builder for this
        display.setGlowing(true);
        display.setGlowColorOverride(Color.AQUA);
        display.setBrightness(new Display.Brightness(15, 15));
        this.displayID = new BlockDisplayID(display.getUniqueId());
    }

    private BlockDisplay getDisplay() {
        return (BlockDisplay) Bukkit.getEntity(displayID.get());
    }

    @Override
    public void tick() {
        getDisplay().teleport(getDisplay().getLocation().add(velocity));
        ageTicks++;
    }

    @Override
    public void remove() {
        getDisplay().remove();
    }

    @Override
    public boolean expired() {
        return ageTicks > lifespanTicks;
    }
}

