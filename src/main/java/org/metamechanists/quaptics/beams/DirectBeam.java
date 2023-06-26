package org.metamechanists.quaptics.beams;

import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.utils.id.TickerID;

public class DirectBeam {
    private final DirectTicker ticker;

    public DirectBeam(DirectTicker ticker) {
        this.ticker = ticker;
    }
    private DirectBeam(TickerID ID) {
        this.ticker = DirectTicker.fromID(new TickerID(ID.get()));
    }

    public static DirectBeam fromID(TickerID ID) {
        return new DirectBeam(ID);
    }

    public TickerID getID() {
        return new TickerID(ticker.getID().get());
    }

    public void deprecate() {
        DeprecatedTickerStorage.deprecate(ticker);
    }
}
