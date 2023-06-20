package org.metamechanists.death_lasers.beams.beam;


public abstract class Beam {
    public abstract void deprecate();
    public abstract boolean readyToRemove();
    public abstract void remove();
    public abstract void tick();
}
