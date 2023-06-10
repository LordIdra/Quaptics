package org.metamechanists.death_lasers.lasers.storage;

import org.metamechanists.death_lasers.lasers.beam.Beam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BeamGroup {

    private final Map<String, Beam> beams;
    private final List<Beam> deprecatedBeams = new ArrayList<>();

    public BeamGroup(String key, Beam value) {
        beams = Collections.singletonMap(key, value);
    }

    public void tick() {
        beams.values().forEach(Beam::tick);
        deprecatedBeams.stream()
                .filter(Beam::readyToRemove)
                .forEach(beam -> {
                    beam.remove();
                    deprecatedBeams.remove(beam);
                });
    }

    public void setPowered(boolean powered) {
        beams.values().forEach(beam -> beam.setPowered(powered));
    }
    public void setPowered(String key, boolean powered) {
        beams.get(key).setPowered(powered);
    }

    public void deprecateAllBeams() {
        beams.keySet().forEach(this::deprecate);
    }
    public void deprecate(String key) {
        setPowered(key, false);
        deprecatedBeams.add(beams.remove(key));
    }

    public void hardRemoveAllBeams() {
        beams.values().forEach(Beam::remove);
    }

    public boolean readyToRemove() {
        return beams.isEmpty() && deprecatedBeams.isEmpty();
    }
}
