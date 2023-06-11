package org.metamechanists.death_lasers.lasers.ticker.ticker.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import dev.sefiraat.sefilib.misc.RotationFace;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
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
        final float rotationXZ = (float)Math.toDegrees(Math.acos(displacement.normalize().getZ() / displacement.normalize().getX()));
        DEATH_LASERS.getInstance().getLogger().info(Objects.toString(rotationXZ));
        this.lifespanTicks = lifespanTicks;
        this.velocity = target.clone().toVector()
                .subtract(source.toVector())
                .multiply(1.0/lifespanTicks);
        this.display = displayBuilder
                .setLocation(source)
                .setDisplayHeight(0.1F)
                .setDisplayWidth(0.1F)
                .setTransformation(new TransformationBuilder()
                        .scale(scale, scale, scale)
                        .firstRotation(RotationFace.TOP, rotationXZ)
                        .translation(-scale/2, -scale/2, -scale/2)
                        .build())
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

