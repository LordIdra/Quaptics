package org.metamechanists.death_lasers.lasers.ticker.factory.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Location;
import org.metamechanists.death_lasers.lasers.ticker.factory.LaserBlockDisplayTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.ticker.interval.IntervalShrinkingLinearTimeTicker;

public class IntervaShrinkingLinearTimeTickerFactory implements LaserBlockDisplayTickerFactory {
    private final BlockDisplayBuilder displayBuilder;
    private final Location source;
    private final Location target;
    private final int lifespanTicks;

    public IntervaShrinkingLinearTimeTickerFactory(BlockDisplayBuilder displayBuilder, Location source, Location target, int lifespanTicks) {
        this.displayBuilder = displayBuilder;
        this.source = source;
        this.target = target;
        this.lifespanTicks = lifespanTicks;
    }

    public IntervalShrinkingLinearTimeTicker build() {
        return new IntervalShrinkingLinearTimeTicker(displayBuilder, source.clone(), target.clone(), lifespanTicks);
    }
}
