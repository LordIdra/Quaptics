package org.metamechanists.death_lasers.lasers.beam;

import org.metamechanists.death_lasers.lasers.DeprecatedBeams;
import org.metamechanists.death_lasers.lasers.SpawnTimer;
import org.metamechanists.death_lasers.lasers.ticker.factory.LaserBlockDisplayTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.factory.IntervalLinearTimeTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IntervalBlockDisplayBeam extends Beam {
    private boolean deprecated = false;
    private final LaserBlockDisplayTickerFactory tickerFactory;
    private final SpawnTimer timer;
    private final Queue<LaserBlockDisplayTicker> tickers = new ConcurrentLinkedQueue<>();

    public IntervalBlockDisplayBeam(IntervalLinearTimeTickerFactory tickerFactory, SpawnTimer timer) {
        this.tickerFactory = tickerFactory;
        this.timer = timer;
    }

    @Override
    public boolean readyToRemove() {
        return tickers.isEmpty();
    }

    @Override
    public void remove() {
        tickers.forEach(LaserBlockDisplayTicker::remove);
    }

    @Override
    public void deprecate() {
        DeprecatedBeams.add(this);deprecated = true;
    }

    @Override
    public void tick() {
        if (!deprecated && timer.update()) {
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
