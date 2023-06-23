package org.metamechanists.death_lasers.beams.ticker.ticker;

public interface DisplayTicker {
    void tick();
    boolean expired();
    void remove();
}
