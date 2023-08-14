package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class DirectBeamId extends ComplexCustomId {
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
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof BlockDisplay;
    }
    @Override
    public Optional<DirectBeam> get() {
        return isValid()
                ? QuapticCache.getBeam(this)
                : Optional.empty();
    }
}
