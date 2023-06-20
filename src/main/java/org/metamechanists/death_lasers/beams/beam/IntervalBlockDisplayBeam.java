package org.metamechanists.death_lasers.beams.beam;

import org.metamechanists.death_lasers.beams.DeprecatedBeamStorage;
import org.metamechanists.death_lasers.beams.SpawnTimer;
import org.metamechanists.death_lasers.beams.ticker.factory.LaserBlockDisplayTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.factory.IntervalLinearTimeTickerFactory;
import org.metamechanists.death_lasers.beams.ticker.ticker.LaserBlockDisplayTicker;

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
        DeprecatedBeamStorage.add(this);deprecated = true;
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
