package org.metamechanists.death_lasers.lasers.beam;


public interface Beam {
    boolean readyToRemove();
    void remove();
    void tick();
}
