package org.metamechanists.quaptics.beams;

import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.utils.id.TickerId;

import java.util.Optional;

public class Beam {
    private final @Nullable DirectTicker ticker;

    public Beam(@Nullable final DirectTicker ticker) {
        this.ticker = ticker;
    }
    public Beam(@NotNull final TickerId id) {
        ticker = id.get().isPresent() ? id.get().get() : null;
    }

    public Optional<TickerId> getId() {
        return Optional.ofNullable(ticker).map(directTicker -> new TickerId(directTicker.getID()));
    }

    public void deprecate() {
        if (ticker != null) {
            DeprecatedTickerStorage.deprecate(ticker);
        }
    }

    public void setMaterial(final Material material) {
        if (ticker != null) {
            ticker.setMaterial(material);
        }
    }

    public void setRadius(final Location source, final Location target, final float radius) {
        if (ticker != null) {
            ticker.setRadius(source, target, radius);
        }
    }
}
