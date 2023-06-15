package org.metamechanists.death_lasers.lasers.ticker.ticker.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;
import org.metamechanists.death_lasers.utils.ScaryMathsUtils;

public class IntervalLinearTimeTicker implements LaserBlockDisplayTicker {
    private static final float SCALE = 0.1F;
    private final int lifespanTicks;
    private final Vector velocity;
    private final BlockDisplay display;
    private int ageTicks = 0;

    public IntervalLinearTimeTicker(BlockDisplayBuilder displayBuilder, Location source, Location target, int lifespanTicks) {
        final Vector displacement = ScaryMathsUtils.getDisplacement(source, target);
        float verticalRotation = ScaryMathsUtils.getVerticalRotation(source, target);
        float horizontalRotation = ScaryMathsUtils.getHorizontalRotation(source, target);

        final Vector offset = new Vector(-SCALE/2, -SCALE/2, -SCALE/2)
                .rotateAroundY(horizontalRotation)
                .rotateAroundAxis(new Vector(Math.cos(horizontalRotation), 0, Math.sin(horizontalRotation)), verticalRotation);

        this.lifespanTicks = lifespanTicks;
        this.velocity = displacement.clone().multiply(1.0/lifespanTicks);
        this.display = displayBuilder
                .setLocation(source.clone().add(offset))
                .setDisplayHeight(0.1F)
                .setDisplayWidth(0.1F)
                .setTransformation(new Transformation(
                        new Vector3f(),
                        new AxisAngle4f(horizontalRotation, 0, 1, 0),
                        new Vector3f(SCALE, SCALE, SCALE),
                        new AxisAngle4f(verticalRotation, 1, 0, 0)))
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

