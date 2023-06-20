package org.metamechanists.death_lasers.beams.beam;

import org.metamechanists.death_lasers.beams.ticker.factory.DirectSinglePulseTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.ticker.DirectSinglePulseTicker;

public class DirectBlockDisplayBeam extends Beam {
    private DirectSinglePulseTicker ticker;

    public DirectBlockDisplayBeam(DirectSinglePulseTickerFactory tickerFactory) {
        this.ticker = tickerFactory.build();
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
    public void deprecate() {
        remove();
    }

    @Override
    public void tick() {}
}
