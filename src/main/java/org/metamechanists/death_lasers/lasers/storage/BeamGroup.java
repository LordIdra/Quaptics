package org.metamechanists.death_lasers.lasers.storage;

import org.metamechanists.death_lasers.lasers.beam.Beam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BeamGroup {

    private final Map<String, Beam> beams;
    private final List<String> toRemove = new ArrayList<>();

    public BeamGroup(String key, Beam value) {
        beams = Collections.singletonMap(key, value);
    }

    public void tick() {
        beams.values().forEach(Beam::tick);
        // Remove expired beeams
        toRemove.stream().filter(key -> beams.get(key).readyToRemove()).forEach(beams::remove);
    }

    public void setPowered(boolean powered) {
        beams.values().forEach(beam -> beam.setPowered(powered));
    }
    public void setPowered(String key, boolean powered) {
        beams.get(key).setPowered(powered);
    }

    public void remove(String key) {
        toRemove.add(key);
    }

    public void hardRemoveAllBeams() {
        beams.values().forEach(Beam::remove);
    }

    public boolean readyToRemove() {
        return toRemove.isEmpty();
    }
}
