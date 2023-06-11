package org.metamechanists.death_lasers.lasers.ticker.factory.interval;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Location;
import org.metamechanists.death_lasers.lasers.ticker.factory.LaserBlockDisplayTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.ticker.interval.IntervalLinearVelocityTicker;

public class IntervalLinearVelocityTickerFactory implements LaserBlockDisplayTickerFactory {
    private final BlockDisplayBuilder displayBuilder;
    private final Location source;
    private final Location target;
    private final float speed;

    public IntervalLinearVelocityTickerFactory(BlockDisplayBuilder displayBuilder, Location source, Location target, float speed) {
        this.displayBuilder = displayBuilder;
        this.source = source;
        this.target = target;
        this.speed = speed;
    }

    public IntervalLinearVelocityTicker build() {
        return new IntervalLinearVelocityTicker(displayBuilder, source.clone(), target.clone(), speed);
    }
}
