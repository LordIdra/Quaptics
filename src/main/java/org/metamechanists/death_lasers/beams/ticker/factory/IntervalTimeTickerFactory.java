package org.metamechanists.death_lasers.beams.ticker.factory;

import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.death_lasers.beams.ticker.ticker.IntervalTimeTicker;

public class IntervalTimeTickerFactory implements DisplayTickerFactory {
    private final Material material;
    private final Location source;
    private final Location target;
    private final int lifespanTicks;

    public IntervalTimeTickerFactory(Material material, Location source, Location target, int lifespanTicks) {
        this.material = material;
        this.source = source.clone();
        this.target = target.clone();
        this.lifespanTicks = lifespanTicks;
    }

    public IntervalTimeTicker build() {
        return new IntervalTimeTicker(material, source.clone(), target.clone(), lifespanTicks);
    }
}
