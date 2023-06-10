package org.metamechanists.death_lasers.lasers.ticker.factory;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Location;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LinearTimeTicker;

public class LinearTimeTickerFactory implements LaserBlockDisplayTickerFactory {
    private final BlockDisplayBuilder displayBuilder;
    private final Location source;
    private final Location target;
    private final int lifespanTicks;

    public LinearTimeTickerFactory(BlockDisplayBuilder displayBuilder, Location source, Location target, int lifespanTicks) {
        this.displayBuilder = displayBuilder;
        this.source = source;
        this.target = target;
        this.lifespanTicks = lifespanTicks;
    }

    public LinearTimeTicker build() {
        return new LinearTimeTicker(displayBuilder, source.clone(), target.clone(), lifespanTicks);
    }
}
