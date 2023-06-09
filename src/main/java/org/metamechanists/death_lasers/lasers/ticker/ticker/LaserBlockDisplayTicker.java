package org.metamechanists.death_lasers.lasers.ticker.ticker;

public interface LaserBlockDisplayTicker {
    void tick();
    boolean expired();
    void remove();
}
