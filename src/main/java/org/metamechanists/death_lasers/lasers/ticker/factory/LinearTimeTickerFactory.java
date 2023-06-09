package org.metamechanists.death_lasers.lasers.ticker.factory;

import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LinearTimeTicker;

public class LinearTimeTickerFactory implements LaserBlockDisplayTickerFactory {
    private final BlockDisplay display;
    private final Location source;
    private final Location target;
    private final int lifespanTicks;

    public LinearTimeTickerFactory(BlockDisplay display, Location source, Location target, int lifespanTicks) {
        this.display = display;
        this.source = source;
        this.target = target;
        this.lifespanTicks = lifespanTicks;
    }

    public LinearTimeTicker build() {
        return new LinearTimeTicker(display, source, target, lifespanTicks);
    }
}
