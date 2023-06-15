package org.metamechanists.death_lasers.lasers.ticker.ticker.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;

public class IntervalLinearTimeTicker implements LaserBlockDisplayTicker {
    private static final float SCALE = 0.1F;
    private final int lifespanTicks;
    private final Vector velocity;
    private final BlockDisplay display;
    private int ageTicks = 0;

    public IntervalLinearTimeTicker(BlockDisplayBuilder displayBuilder, Location source, Location target, int lifespanTicks) {
        final Vector displacement = target.clone().subtract(source).toVector();
        final Vector direction = displacement.clone().normalize();

        final float rotationXZ = (float) Math.atan(direction.getX() / direction.getZ());
        final Vector vectorInXZPlaneAtSameRotationAsDisplacement = new Vector(0, 0, 1).rotateAroundY(rotationXZ);
        float rotationXY = vectorInXZPlaneAtSameRotationAsDisplacement.angle(displacement);

        source.add(SCALE*Math.cos(rotationXZ), ConnectionPoint.SCALE/2 -SCALE/2, SCALE*Math.sin(rotationXZ));
        target.add(SCALE*Math.cos(rotationXZ), ConnectionPoint.SCALE/2 -SCALE/2, SCALE*Math.sin(rotationXZ));

        source.getWorld().spawnParticle(Particle.REDSTONE, source, 50, new Particle.DustOptions(Color.RED, 0.1F));
        target.getWorld().spawnParticle(Particle.REDSTONE, target, 50, new Particle.DustOptions(Color.GREEN, 0.1F));

        if (displacement.getY() > 0) {
            rotationXY = -rotationXY;
        }

        this.lifespanTicks = lifespanTicks;
        this.velocity = displacement.clone().multiply(1.0/lifespanTicks);
        this.display = displayBuilder
                .setLocation(source)
                .setDisplayHeight(0.1F)
                .setDisplayWidth(0.1F)
                .setTransformation(new Transformation(
                        new Vector3f(),
                        new AxisAngle4f(rotationXZ, 0, 1, 0),
                        new Vector3f(SCALE, SCALE, SCALE),
                        new AxisAngle4f(rotationXY, 1.0F, 0.0F, 0.0F)))
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

