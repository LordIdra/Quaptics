package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.metamechanists.quaptics.beams.beam.DirectBeam;

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
    public Optional<DirectBeam> get() {
        return (Bukkit.getEntity(getUUID()) instanceof BlockDisplay)
                ? Optional.of(new DirectBeam(this))
                : Optional.empty();
    }
}
