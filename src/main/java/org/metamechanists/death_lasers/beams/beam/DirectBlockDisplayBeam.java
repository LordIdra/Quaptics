package org.metamechanists.death_lasers.beams.beam;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.beams.DeprecatedTickerStorage;
import org.metamechanists.death_lasers.beams.ticker.factory.DirectSinglePulseTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.ticker.DirectSinglePulseTicker;

import java.util.HashMap;
import java.util.Map;

public class DirectBlockDisplayBeam extends Beam implements ConfigurationSerializable {
    private final DirectSinglePulseTicker ticker;

    public DirectBlockDisplayBeam(DirectSinglePulseTickerFactory factory) {
        this.ticker = factory.build();
    }

    private DirectBlockDisplayBeam(DirectSinglePulseTicker ticker) {
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

    public static DirectBlockDisplayBeam deserialize(Map<String, Object> map) {
        return new DirectBlockDisplayBeam((DirectSinglePulseTicker) map.get("ticker"));
    }
}
