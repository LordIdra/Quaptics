package org.metamechanists.death_lasers.beams.beam;

import org.metamechanists.death_lasers.beams.SpawnTimer;
import org.metamechanists.death_lasers.beams.ticker.factory.IntervalTimeTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.factory.DisplayTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.ticker.DisplayTicker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IntervalBeam extends Beam {
    // TODO make this serializable?
    private final DisplayTickerFactory tickerFactory;
    private final SpawnTimer timer;
    private final Queue<DisplayTicker> tickers = new ConcurrentLinkedQueue<>();

    public IntervalBeam(IntervalTimeTickerFactory tickerFactory, SpawnTimer timer) {
        this.tickerFactory = tickerFactory;
        this.timer = timer;
    }

    @Override
    public void deprecate() {
        tickers.forEach(DisplayTicker::remove);
    }

    @Override
    public void tick() {
        if (timer.update()) {
            tickers.add(tickerFactory.build());
        }

        tickers.stream()
                .filter(DisplayTicker::expired)
                .forEach(ticker -> {
                    ticker.remove();
                    tickers.remove(ticker);
                });
        tickers.forEach(DisplayTicker::tick);
    }
}
