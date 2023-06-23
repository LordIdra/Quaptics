package org.metamechanists.quaptics.beams.ticker.factory;

import org.bukkit.Location;
import org.bukkit.Material;
import org.metamechanists.quaptics.beams.ticker.ticker.DirectTicker;

public class DirectTickerFactory implements DisplayTickerFactory {
    private final Material material;
    private final Location source;
    private final Location target;
    private final float size;

    public DirectTickerFactory(Material material, Location source, Location target, float size) {
        this.material = material;
        this.source = source.clone();
        this.target = target.clone();
        this.size = size;
    }

    public DirectTicker build() {
        return new DirectTicker(material, source.clone(), target.clone(), size);
    }
}
