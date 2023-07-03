package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.metamechanists.quaptics.beams.beam.Beam;
import org.metamechanists.quaptics.storage.QuapticCache;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class BeamId extends CustomId {
    public BeamId() {
        super();
    }
    public BeamId(final CustomId id) {
        super(id);
    }
    public BeamId(final String uuid) {
        super(uuid);
    }
    public BeamId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public Optional<Beam> get() {
        return (Bukkit.getEntity(getUUID()) instanceof BlockDisplay)
                ? QuapticCache.getBeam(this)
                : Optional.empty();
    }
}
