package org.metamechanists.quaptics.utils.id;

import org.bukkit.Bukkit;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;

import java.util.UUID;

@SuppressWarnings("unused")
public class TickerID extends CustomID {
    public TickerID() {
        super();
    }
    public TickerID(CustomID ID) {
        super(ID);
    }
    public TickerID(String string) {
        super(string);
    }
    public TickerID(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable DirectTicker get() {
        final Entity entity = Bukkit.getEntity(getUUID());
        return (entity instanceof BlockDisplay)
                ? new DirectTicker(this)
                : null;
    }
}
