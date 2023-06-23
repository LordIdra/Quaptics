package org.metamechanists.quaptics.beams.ticker.factory;

import org.bukkit.Location;
import org.bukkit.Material;
import org.joml.Vector3f;
import org.metamechanists.quaptics.beams.ticker.ticker.IntervalTimeTicker;

public class IntervalTimeTickerFactory implements DisplayTickerFactory {
    private final Material material;
    private final Location source;
    private final Location target;
    private final Vector3f scale;
    private final int lifespanTicks;

    public IntervalTimeTickerFactory(Material material, Location source, Location target, Vector3f scale, int lifespanTicks) {
        this.material = material;
        this.source = source.clone();
        this.target = target.clone();
        this.scale = scale;
        this.lifespanTicks = lifespanTicks;
    }

    public IntervalTimeTicker build() {
        return new IntervalTimeTicker(material, source.clone(), target.clone(), scale, lifespanTicks);
    }
}
