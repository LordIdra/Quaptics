package org.metamechanists.death_lasers.beams.ticker.ticker;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.Transformations;
import org.metamechanists.death_lasers.utils.builders.BlockDisplayBuilder;
import org.metamechanists.death_lasers.utils.id.BlockDisplayID;

public class IntervalVelocityTicker implements DisplayTicker {
    // Not quite 1 to prevent Z-fighting with connection points
    private static final Vector3f DISPLAY_SIZE = new Vector3f(0.095F, 0.095F, 0.20F);
    private final Vector3f velocity;
    private final BlockDisplayID displayID;
    private final int lifespanTicks;
    private int ageTicks = 0;

    public IntervalVelocityTicker(Material material, Location source, Location target, float speed) {
        this.velocity = Transformations.getDisplacement(source, target).normalize().mul(speed);
        this.lifespanTicks = (int)(Transformations.getDisplacement(source, target).length() / speed) + 1;
        this.displayID = new BlockDisplayID(new BlockDisplayBuilder(source)
                .setMaterial(material)
                .setTransformation(Transformations.lookAlong(DISPLAY_SIZE, Transformations.getDirection(source, target)))
                .setGlow(Color.AQUA)
                .setBrightness(15)
                .build()
                .getUniqueId());
    }

    private BlockDisplay getDisplay() {
        return (BlockDisplay) Bukkit.getEntity(displayID.get());
    }

    @Override
    public void tick() {
        getDisplay().teleport(getDisplay().getLocation().add(Vector.fromJOML(velocity)));
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

