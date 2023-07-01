package org.metamechanists.quaptics.beams;

import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.utils.id.TickerId;

public class DirectBeam {
    private final DirectTicker ticker;

    public DirectBeam(DirectTicker ticker) {
        this.ticker = ticker;
    }
    public DirectBeam(@NotNull TickerId id) {
        this.ticker = id.get();
    }

    public TickerId getId() {
        return new TickerId(ticker.getID());
    }

    public void deprecate() {
        DeprecatedTickerStorage.deprecate(ticker);
    }
}
