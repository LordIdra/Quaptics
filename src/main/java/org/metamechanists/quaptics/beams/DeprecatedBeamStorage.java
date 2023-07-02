package org.metamechanists.quaptics.beams;

import org.metamechanists.quaptics.beams.beam.Beam;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class DeprecatedBeamStorage {
    private static final Collection<Beam> beams = new ConcurrentLinkedQueue<>();

    private DeprecatedBeamStorage() {}

    public static void deprecate(final Beam beam) {
        beams.add(beam);
    }

    public static void tick() {
        beams.forEach(Beam::tick);
        beams.stream().filter(Beam::expired).forEach(beam -> {
            beam.remove();
            beams.remove(beam);
        });
    }
}
