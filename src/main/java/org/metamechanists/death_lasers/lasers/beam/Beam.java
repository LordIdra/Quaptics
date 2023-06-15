package org.metamechanists.death_lasers.lasers.beam;


import lombok.Setter;

public abstract class Beam {
    @Setter
    protected boolean powered = false;
    public abstract boolean readyToRemove();
    public abstract void remove();
    public abstract void tick();
}
