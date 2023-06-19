package org.metamechanists.death_lasers.lasers.beam;


import lombok.Getter;
import lombok.Setter;

public abstract class Beam {
    @Setter
    @Getter
    protected boolean powered = false;
    public abstract boolean readyToRemove();
    public abstract void remove();
    public abstract void tick();
}
