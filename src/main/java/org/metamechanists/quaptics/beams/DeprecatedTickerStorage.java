package org.metamechanists.quaptics.beams;

import org.metamechanists.quaptics.beams.ticker.DisplayTicker;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class DeprecatedTickerStorage {
    private static final Collection<DisplayTicker> tickers = new ConcurrentLinkedQueue<>();

    private DeprecatedTickerStorage() {}

    public static void deprecate(final DisplayTicker ticker) {
        tickers.add(ticker);
    }

    public static void tick() {
        tickers.forEach(DisplayTicker::tick);
        tickers.stream().filter(DisplayTicker::expired).forEach(ticker -> {
            ticker.remove();
            tickers.remove(ticker);
        });
    }
}
