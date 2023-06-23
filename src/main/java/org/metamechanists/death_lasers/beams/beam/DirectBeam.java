package org.metamechanists.death_lasers.beams.beam;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.beams.DeprecatedTickerStorage;
import org.metamechanists.death_lasers.beams.ticker.factory.DirectTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.ticker.DirectTicker;

import java.util.HashMap;
import java.util.Map;

public class DirectBeam extends Beam implements ConfigurationSerializable {
    private final DirectTicker ticker;

    public DirectBeam(DirectTickerFactory factory) {
        this.ticker = factory.build();
    }
    private DirectBeam(DirectTicker ticker) {
        this.ticker = ticker;
    }

    @Override
    public void deprecate() {
        DeprecatedTickerStorage.deprecate(ticker);
    }

    @Override
    public void tick() {}

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("ticker", ticker);
        return map;
    }

    public static DirectBeam deserialize(Map<String, Object> map) {
        return new DirectBeam((DirectTicker) map.get("ticker"));
    }
}
