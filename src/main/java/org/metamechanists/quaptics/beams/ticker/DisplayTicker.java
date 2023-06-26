package org.metamechanists.quaptics.beams.ticker;

public interface DisplayTicker {
    void tick();
    void remove();
    boolean expired();
}
