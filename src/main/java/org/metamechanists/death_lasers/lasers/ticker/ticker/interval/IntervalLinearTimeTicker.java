package org.metamechanists.death_lasers.lasers.ticker.ticker.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import dev.sefiraat.sefilib.misc.RotationFace;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;

import java.util.Objects;

public class IntervalLinearTimeTicker implements LaserBlockDisplayTicker {
    private final float scale = 0.1F;
    private final int lifespanTicks;
    private final Vector velocity;
    private final BlockDisplay display;
    private int ageTicks = 0;

    public IntervalLinearTimeTicker(BlockDisplayBuilder displayBuilder, Location source, Location target, int lifespanTicks) {
        final Vector displacement = source.clone().subtract(target).toVector();
        final float rotationXZ = (float)Math.atan(displacement.normalize().getX() / displacement.normalize().getZ());
        final float rotationXY = (float)Math.atan(displacement.normalize().getX() / displacement.normalize().getY());

        DEATH_LASERS.getInstance().getLogger().info(Objects.toString(rotationXZ) + " " + Objects.toString(rotationXY));

        this.lifespanTicks = lifespanTicks;
        this.velocity = target.clone().toVector()
                .subtract(source.toVector())
                .multiply(1.0/lifespanTicks);
        this.display = displayBuilder
                .setLocation(source)
                .setDisplayHeight(0.1F)
                .setDisplayWidth(0.1F)
                .setTransformation(new Transformation(
                        new Vector3f(0, 0, 0),
                        new AxisAngle4f(rotationXZ, 0, 1, 0),
                        new Vector3f(scale, scale, scale),
                        new AxisAngle4f(rotationXY, (float)Math.cos(rotationXZ), 0, (float)Math.sin(rotationXZ))))
                .build();
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

