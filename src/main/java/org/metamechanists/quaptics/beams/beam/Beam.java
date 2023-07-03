package org.metamechanists.quaptics.beams.beam;

import org.metamechanists.quaptics.utils.id.BeamId;

public interface Beam {
    BeamId getId();
    void tick();
    void remove();
    boolean expired();
}
