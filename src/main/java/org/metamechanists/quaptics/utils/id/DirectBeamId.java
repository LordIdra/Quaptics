package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.storage.QuapticCache;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class DirectBeamId extends CustomId {
    public DirectBeamId() {
        super();
    }
    public DirectBeamId(final CustomId id) {
        super(id);
    }
    public DirectBeamId(final String uuid) {
        super(uuid);
    }
    public DirectBeamId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public Optional<DirectBeam> get() {
        return (Bukkit.getEntity(getUUID()) instanceof BlockDisplay)
                ? QuapticCache.getBeam(this)
                : Optional.empty();
    }
}
