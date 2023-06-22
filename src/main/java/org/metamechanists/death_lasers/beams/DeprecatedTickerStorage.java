package org.metamechanists.death_lasers.beams;

import org.metamechanists.death_lasers.beams.ticker.ticker.LaserBlockDisplayTicker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DeprecatedTickerStorage {
    private static final Queue<LaserBlockDisplayTicker> tickers = new ConcurrentLinkedQueue<>();

    public static void deprecate(LaserBlockDisplayTicker ticker) {
        tickers.add(ticker);
    }

    public static void tick() {
        tickers.forEach(LaserBlockDisplayTicker::tick);
        tickers.stream().filter(LaserBlockDisplayTicker::expired).forEach(ticker -> {
            ticker.remove();
            tickers.remove(ticker);
        });
    }
}
