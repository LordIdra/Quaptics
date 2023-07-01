package org.metamechanists.quaptics.beams.ticker;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.BlockDisplayId;

import java.util.Optional;

public class IntervalVelocityTicker implements DisplayTicker {
    private final Vector3f velocity;
    private final BlockDisplayId displayID;
    private final int lifespanTicks;
    private int ageTicks;

    public IntervalVelocityTicker(final Material material, final Location source, final Location target, final Vector3f scale, final float speed) {
        super();
        this.velocity = Transformations.getDisplacement(source, target).normalize().mul(speed);
        this.lifespanTicks = (int) (Transformations.getDisplacement(source, target).length() / speed) + 1;
        this.displayID = new BlockDisplayId(new BlockDisplayBuilder(source)
                .setMaterial(material)
                .setTransformation(Transformations.lookAlong(scale, Transformations.getDirection(source, target)))
                .setGlow(Color.AQUA)
                .setBrightness(15)
                .build()
                .getUniqueId());
    }

    private Optional<BlockDisplay> getDisplay() {
        return displayID.get();
    }

    @Override
    public void tick() {
        getDisplay().ifPresent(display -> display.teleport(display.getLocation().add(Vector.fromJOML(velocity))));
        ageTicks++;
    }

    @Override
    public void remove() {
        getDisplay().ifPresent(BlockDisplay::remove);
    }

    @Override
    public boolean expired() {
        return ageTicks > lifespanTicks;
    }
}

