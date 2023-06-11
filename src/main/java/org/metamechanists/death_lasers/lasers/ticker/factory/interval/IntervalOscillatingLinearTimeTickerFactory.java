package org.metamechanists.death_lasers.lasers.ticker.factory.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Location;
import org.metamechanists.death_lasers.lasers.ticker.factory.LaserBlockDisplayTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.ticker.interval.IntervalOscillatingLinearTimeTicker;
import org.metamechanists.death_lasers.lasers.ticker.ticker.interval.IntervalShrinkingLinearTimeTicker;

public class IntervalOscillatingLinearTimeTickerFactory implements LaserBlockDisplayTickerFactory {
    private final BlockDisplayBuilder displayBuilder;
    private final Location source;
    private final Location target;
    private final int lifespanTicks;

    public IntervalOscillatingLinearTimeTickerFactory(BlockDisplayBuilder displayBuilder, Location source, Location target, int lifespanTicks) {
        this.displayBuilder = displayBuilder;
        this.source = source;
        this.target = target;
        this.lifespanTicks = lifespanTicks;
    }

    public IntervalOscillatingLinearTimeTicker build() {
        return new IntervalOscillatingLinearTimeTicker(displayBuilder, source.clone(), target.clone(), 0.5, lifespanTicks);
    }
}
