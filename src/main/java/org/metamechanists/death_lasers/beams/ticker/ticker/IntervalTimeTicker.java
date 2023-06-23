package org.metamechanists.death_lasers.beams.ticker.ticker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.Transformations;
import org.metamechanists.death_lasers.utils.builders.BlockDisplayBuilder;
import org.metamechanists.death_lasers.utils.id.BlockDisplayID;

public class IntervalTimeTicker implements DisplayTicker {
    // Not quite 1 to prevent Z-fighting with connection points
    private static final Vector3f DISPLAY_SIZE = new Vector3f(0.095F, 0.095F, 0.20F);
    private final int lifespanTicks;
    private final Vector3f velocity;
    private final BlockDisplayID displayID;
    private int ageTicks = 0;

    public IntervalTimeTicker(Material material, Location source, Location target, int lifespanTicks) {
        this.lifespanTicks = lifespanTicks;
        this.velocity = Transformations.getDisplacement(source, target).mul(1.0F/lifespanTicks);
        this.displayID = new BlockDisplayID(new BlockDisplayBuilder(source)
                .setMaterial(material)
                .setTransformation(Transformations.lookAlong(DISPLAY_SIZE, Transformations.getDirection(source, target)))
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

