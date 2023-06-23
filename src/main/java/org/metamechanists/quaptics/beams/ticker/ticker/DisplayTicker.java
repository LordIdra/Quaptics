package org.metamechanists.quaptics.beams.ticker.ticker;

public interface DisplayTicker {
    void tick();
    boolean expired();
    void remove();
}
