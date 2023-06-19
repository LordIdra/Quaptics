package org.metamechanists.death_lasers.lasers.beam;

import org.metamechanists.death_lasers.lasers.ticker.factory.DirectSinglePulseTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.ticker.DirectSinglePulseTicker;

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
            ticker = null;
        }
    }

    @Override
    public void setPowered(boolean powered) {
        super.setPowered(powered);

        if (powered && ticker == null) {
            ticker = tickerFactory.build();
        }

        if (!powered && ticker != null) {
            remove();
        }
    }

    @Override
    public void tick() {}
}
