package org.metamechanists.quaptics.beams.beam;

import org.metamechanists.quaptics.beams.DeprecatedTickerStorage;
import org.metamechanists.quaptics.beams.ticker.factory.DirectTickerFactory;
import org.metamechanists.quaptics.beams.ticker.ticker.DirectTicker;
import org.metamechanists.quaptics.utils.id.BeamID;
import org.metamechanists.quaptics.utils.id.TickerID;

public class DirectBeam extends Beam {
    private final DirectTicker ticker;

    public DirectBeam(DirectTickerFactory factory) {
        this.ticker = factory.build();
    }
    private DirectBeam(BeamID ID) {
        this.ticker = DirectTicker.fromID(new TickerID(ID.get()));
    }

    public static DirectBeam fromID(BeamID ID) {
        return new DirectBeam(ID);
    }

    public BeamID getID() {
        return new BeamID(ticker.getID().get());
    }

    @Override
    public void deprecate() {
        DeprecatedTickerStorage.deprecate(ticker);
    }

    @Override
    public void tick() {}
}
