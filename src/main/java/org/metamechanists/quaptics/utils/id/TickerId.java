package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class TickerId extends CustomId {
    public TickerId() {
        super();
    }
    public TickerId(final CustomId id) {
        super(id);
    }
    public TickerId(final String uuid) {
        super(uuid);
    }
    public TickerId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public Optional<DirectTicker> get() {
        return (Bukkit.getEntity(getUUID()) instanceof BlockDisplay)
                ? Optional.of(new DirectTicker(this))
                : Optional.empty();
    }
}
