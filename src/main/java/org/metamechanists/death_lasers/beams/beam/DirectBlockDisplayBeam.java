package org.metamechanists.death_lasers.beams.beam;

import org.metamechanists.death_lasers.beams.ticker.factory.DirectSinglePulseTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.ticker.DirectSinglePulseTicker;

public class DirectBlockDisplayBeam extends Beam {
    private DirectSinglePulseTickerFactory factory;
    private DirectSinglePulseTicker ticker;

    public DirectBlockDisplayBeam(DirectSinglePulseTickerFactory factory) {
        this.factory = factory;
        this.ticker = factory.build();
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

    public void setSize() {
        remove();
        this.ticker = factory.build();
    }
}
