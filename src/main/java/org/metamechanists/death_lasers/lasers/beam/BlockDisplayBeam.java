package org.metamechanists.death_lasers.lasers.beam;

import org.metamechanists.death_lasers.lasers.SpawnTimer;
import org.metamechanists.death_lasers.lasers.ticker.factory.LaserBlockDisplayTickerFactory;
import org.metamechanists.death_lasers.lasers.ticker.ticker.LaserBlockDisplayTicker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockDisplayBeam extends Beam {
    private final LaserBlockDisplayTickerFactory tickerFactory;
    private final SpawnTimer timer;
    private final Queue<LaserBlockDisplayTicker> displays = new ConcurrentLinkedQueue<>();

    public BlockDisplayBeam(LaserBlockDisplayTickerFactory tickerFactory, SpawnTimer timer, boolean powered) {
        this.tickerFactory = tickerFactory;
        this.timer = timer;
        this.powered = powered;
    }

    @Override
    public boolean readyToRemove() {
        return displays.isEmpty();
    }

    @Override
    public void remove() {
        displays.forEach(LaserBlockDisplayTicker::remove);
    }

    @Override
    public void tick() {
        if (powered && timer.Update()) {
            displays.add(tickerFactory.build());
        }

        displays.stream().filter(LaserBlockDisplayTicker::expired).forEach(LaserBlockDisplayTicker::remove);
        displays.forEach(LaserBlockDisplayTicker::tick);
    }
}
