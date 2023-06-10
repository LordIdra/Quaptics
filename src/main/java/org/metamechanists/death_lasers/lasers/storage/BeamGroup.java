package org.metamechanists.death_lasers.lasers.storage;

import org.metamechanists.death_lasers.lasers.beam.Beam;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BeamGroup {

    private final Map<String, Beam> beams = new ConcurrentHashMap<>();
    private final Queue<Beam> deprecatedBeams = new ConcurrentLinkedQueue<>();

    public BeamGroup(String key, Beam value) {
        beams.put(key, value);
    }

    // debug methods
    public int getNumberOfActiveBeams() {
        return beams.size();
    }
    public int getNumberOfActiveBeamTickers() {
        return beams.size();
    }

    public void tick() {
        for (Beam beam : beams.values()) {
            beam.tick();
        }

        for (Beam beam : deprecatedBeams) {
            beam.tick();
        }

        for (Beam beam : deprecatedBeams) {
            if (beam.readyToRemove()) {
                beam.remove();
                deprecatedBeams.remove(beam);
            }
        }
    }

    public void setPowered(boolean powered) {
        for (Beam beam : beams.values()) {
            beam.setPowered(powered);
        }
    }
    public void setPowered(String key, boolean powered) {
        beams.get(key).setPowered(powered);
    }

    public void deprecateAllBeams() {
        for (String s : beams.keySet()) {
            deprecate(s);
        }
    }
    public void deprecate(String key) {
        setPowered(key, false);
        deprecatedBeams.add(beams.remove(key));
    }

    public void hardRemoveAllBeams() {
        for (Beam beam : beams.values()) {
            beam.remove();
        }
    }

    public boolean readyToRemove() {
        return beams.isEmpty() && deprecatedBeams.isEmpty();
    }
}
