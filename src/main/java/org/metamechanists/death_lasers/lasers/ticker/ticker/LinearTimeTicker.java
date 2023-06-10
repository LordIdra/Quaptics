package org.metamechanists.death_lasers.lasers.ticker.ticker;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.DEATH_LASERS;

import java.util.Objects;

public class LinearTimeTicker implements LaserBlockDisplayTicker {
    private final int lifespanTicks;
    private final Vector velocity;
    private final BlockDisplay display;
    private int ageTicks = 0;

    public LinearTimeTicker(BlockDisplayBuilder displayBuilder, Location source, Location target, int lifespanTicks) {
        this.lifespanTicks = lifespanTicks;
        velocity = target.toVector()
                .subtract(source.toVector())
                .multiply(1.0/lifespanTicks);
        this.display = displayBuilder
                .setLocation(source)
                .setTransformation(new TransformationBuilder()
                        .scale(0.2F, 0.2F, 0.2F)
                        .build())
                .build();
    }


    @Override
    public void tick() {
        DEATH_LASERS.getInstance().getLogger().severe(Objects.toString(ageTicks));
        display.teleport(display.getLocation().add(velocity));
        ageTicks++;
    }

    @Override
    public void remove() {
        display.remove();
    }

    @Override
    public boolean expired() {
        DEATH_LASERS.getInstance().getLogger().severe("Checking expired: " + Objects.toString(ageTicks));
        return ageTicks >= lifespanTicks;
    }
}

