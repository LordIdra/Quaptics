package org.metamechanists.death_lasers.lasers.ticker.ticker.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;

public class IntervalLinearVelocityTicker implements LaserBlockDisplayTicker {
    private final double lifespanTicks;
    private final Vector velocity;
    private final BlockDisplay display;
    private int ageTicks = 0;

    public IntervalLinearVelocityTicker(BlockDisplayBuilder displayBuilder, Location source, Location target, float speed) {
        final Vector displacement = target.clone().subtract(source).toVector();
        this.lifespanTicks = displacement.length() / speed;
        this.velocity = displacement.clone().normalize().multiply(speed);
        this.display = displayBuilder
                .setLocation(source)
                .setDisplayHeight(0.1F)
                .setDisplayWidth(0.1F)
                .setTransformation(new TransformationBuilder().scale(0.1F, 0.1F, 0.1F).build())
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

