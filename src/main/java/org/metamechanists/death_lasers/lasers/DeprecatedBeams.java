package org.metamechanists.death_lasers.lasers;

import org.metamechanists.death_lasers.lasers.beam.Beam;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DeprecatedBeams {
    private static final Queue<Beam> beams = new ConcurrentLinkedQueue<>();

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
