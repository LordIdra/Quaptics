package org.metamechanists.death_lasers.lasers.ticker.ticker.direct;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;

public class StaticSinglePulseTicker implements LaserBlockDisplayTicker {
    private final BlockDisplay display;
    private int ageTicks = 0;

    public StaticSinglePulseTicker(BlockDisplayBuilder displayBuilder, Location source, Location target) {
        this.display = displayBuilder
                .setLocation(source)
                .setDisplayHeight(0.1F)
                .setDisplayWidth(0.1F)
                .setTransformation(new TransformationBuilder().scale(0.1F, 0.1F, 0.1F).build())
                .build();
    }


    @Override
    public void tick() {
        ageTicks++;
    }

    @Override
    public void remove() {
        display.remove();
    }

    @Override
    public boolean expired() {
        return false;
    }
}

