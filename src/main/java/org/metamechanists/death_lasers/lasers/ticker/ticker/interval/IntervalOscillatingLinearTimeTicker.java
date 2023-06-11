package org.metamechanists.death_lasers.lasers.ticker.ticker.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;

public class IntervalOscillatingLinearTimeTicker implements LaserBlockDisplayTicker {
    private static final float SCALE = 0.1F;
    private final double amplitude;
    private final double period;
    private final int lifespanTicks;
    private final Vector velocity;
    private final BlockDisplay display;
    private int ageTicks = 0;

    public IntervalOscillatingLinearTimeTicker(BlockDisplayBuilder displayBuilder, Location source, Location target, double amplitude, int lifespanTicks) {
        final Vector displacement = target.clone().subtract(source).toVector();
        final Vector direction = displacement.clone().normalize();
        float rotationXZ = (float) Math.atan(direction.getX() / direction.getZ());
        Vector vectorInXZPlaneAtSameRotationAsDisplacement = new Vector(0, 0, 1).rotateAroundY(rotationXZ);
        float rotationXY = vectorInXZPlaneAtSameRotationAsDisplacement.angle(displacement);

        if (displacement.getY() > 0) {
            rotationXY = -rotationXY;
        }

        this.amplitude = amplitude;
        this.lifespanTicks = lifespanTicks;
        this.velocity = displacement.clone().multiply(1.0/lifespanTicks);
        this.period = this.velocity.length();
        this.display = displayBuilder
                .setLocation(source)
                .setDisplayHeight(0.1F)
                .setDisplayWidth(0.1F)
                .setTransformation(new Transformation(
                        new Vector3f(0, 0, 0),
                        new AxisAngle4f(rotationXZ, 0, 1, 0),
                        new Vector3f(SCALE, SCALE, SCALE),
                        new AxisAngle4f(rotationXY, 1.0F, 0.0F, 0.0F)))
                .build();
    }

    @Override
    public void tick() {
        final double phase = (this.ageTicks / this.period) * 8 * Math.PI;
        display.teleport(display.getLocation().add(velocity).add(new Vector(0, 0, Math.sin(phase) * amplitude)));
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
