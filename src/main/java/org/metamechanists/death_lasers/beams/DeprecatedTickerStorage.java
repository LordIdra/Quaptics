package org.metamechanists.death_lasers.beams;

import org.metamechanists.death_lasers.beams.ticker.ticker.DisplayTicker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DeprecatedTickerStorage {
    private static final Queue<DisplayTicker> tickers = new ConcurrentLinkedQueue<>();

    public static void deprecate(DisplayTicker ticker) {
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
