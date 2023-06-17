package org.metamechanists.death_lasers.lasers.ticker.factory.interval;

import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.death_lasers.lasers.ticker.factory.LaserBlockDisplayTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.ticker.interval.IntervalLinearTimeTicker;

public class IntervalLinearTimeTickerFactory implements LaserBlockDisplayTickerFactory {
    private final Material material;
    private final Location source;
    private final Location target;
    private final int lifespanTicks;

    public IntervalLinearTimeTickerFactory(Material material, Location source, Location target, int lifespanTicks) {
        this.material = material;
        this.source = source.clone();
        this.target = target.clone();
        this.lifespanTicks = lifespanTicks;
    }

    public IntervalLinearTimeTicker build() {
        return new IntervalLinearTimeTicker(material, source.clone(), target.clone(), lifespanTicks);
    }
}
