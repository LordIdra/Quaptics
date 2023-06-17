package org.metamechanists.death_lasers.lasers.beam;

import org.metamechanists.death_lasers.lasers.ticker.factory.direct.DirectSinglePulseTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.ticker.direct.DirectSinglePulseTicker;

public class DirectBlockDisplayBeam extends Beam {
    private final DirectSinglePulseTickerFactory tickerFactory;
    private DirectSinglePulseTicker ticker = null;

    public DirectBlockDisplayBeam(DirectSinglePulseTickerFactory tickerFactory) {
        this.tickerFactory = tickerFactory;
    }

    @Override
    public boolean readyToRemove() {
        return true;
    }

    @Override
    public void remove() {
        if (ticker != null) {
            ticker.remove();
        }
    }

    @Override
    public void tick() {
        if (powered && ticker == null) {
            ticker = tickerFactory.build();
        }

        if (!powered && ticker != null) {
            ticker = null;
        }
    }
}
