package org.metamechanists.death_lasers.beams.ticker.ticker;

public interface LaserBlockDisplayTicker {
    void tick();
    boolean expired();
    void remove();
}
