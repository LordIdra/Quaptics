package org.metamechanists.death_lasers.lasers;

import org.metamechanists.death_lasers.lasers.beam.Beam;

import java.util.ArrayList;
import java.util.List;

public class DeprecatedBeams {
    private static final List<Beam> beams = new ArrayList<>();

    public static void add(Beam beam) {
        beam.setPowered(false);
        beams.add(beam);
    }

    public static void killAllBeams() {
        beams.forEach(Beam::remove);
    }

    public static void tick() {
        beams.forEach(Beam::tick);
        beams.stream().filter(Beam::readyToRemove).forEach(beam -> {
            beam.remove();
            beams.remove(beam);
        });
    }
}
