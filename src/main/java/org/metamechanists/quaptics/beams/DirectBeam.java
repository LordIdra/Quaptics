package org.metamechanists.quaptics.beams;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.utils.id.TickerId;

import java.util.Optional;

public class DirectBeam {
    private final @Nullable DirectTicker ticker;

    public DirectBeam(@Nullable final DirectTicker ticker) {
        this.ticker = ticker;
    }
    public DirectBeam(@NotNull final TickerId id) {
        ticker = id.get().isPresent() ? id.get().get() : null;
    }

    public Optional<TickerId> getId() {
        return Optional.ofNullable(ticker).map(directTicker -> new TickerId(directTicker.getID()));
    }

    public void deprecate() {
        if (ticker != null) {
            DeprecatedTickerStorage.deprecate(ticker);
        }
    }
}
