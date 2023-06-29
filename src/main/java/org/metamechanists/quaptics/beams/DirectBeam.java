package org.metamechanists.quaptics.beams;

import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.utils.id.TickerID;

public class DirectBeam {
    private final DirectTicker ticker;

    public DirectBeam(DirectTicker ticker) {
        this.ticker = ticker;
    }
    public DirectBeam(@NotNull TickerID ID) {
        this.ticker = ID.get();
    }

    public TickerID getID() {
        return new TickerID(ticker.getID());
    }

    public void deprecate() {
        DeprecatedTickerStorage.deprecate(ticker);
    }
}
