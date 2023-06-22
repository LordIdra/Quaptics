package org.metamechanists.death_lasers.beams.beam;

import org.metamechanists.death_lasers.beams.SpawnTimer;
import org.metamechanists.death_lasers.beams.ticker.factory.IntervalLinearTimeTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.factory.LaserBlockDisplayTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.ticker.LaserBlockDisplayTicker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IntervalBlockDisplayBeam extends Beam {
    private final LaserBlockDisplayTickerFactory tickerFactory;
    private final SpawnTimer timer;
    private final Queue<LaserBlockDisplayTicker> tickers = new ConcurrentLinkedQueue<>();

    public IntervalBlockDisplayBeam(IntervalLinearTimeTickerFactory tickerFactory, SpawnTimer timer) {
        this.tickerFactory = tickerFactory;
        this.timer = timer;
    }

    @Override
    public void deprecate() {
        tickers.forEach(LaserBlockDisplayTicker::remove);
    }

    @Override
    public void tick() {
        if (timer.update()) {
            tickers.add(tickerFactory.build());
        }

        tickers.stream()
                .filter(LaserBlockDisplayTicker::expired)
                .forEach(ticker -> {
                    ticker.remove();
                    tickers.remove(ticker);
                });
        tickers.forEach(LaserBlockDisplayTicker::tick);
    }
}
