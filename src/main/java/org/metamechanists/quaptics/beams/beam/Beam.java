package org.metamechanists.quaptics.beams.beam;

public interface Beam {
    void tick();
    void remove();
    boolean expired();
}
