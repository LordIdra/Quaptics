package org.metamechanists.quaptics.beams.ticker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.BlockDisplayId;
import org.metamechanists.quaptics.utils.id.TickerId;

import java.util.Optional;

public class DirectTicker implements DisplayTicker {
    private final BlockDisplayId displayId;

    public DirectTicker(final Material material, @NotNull final Location source, final Location target, final float radius) {
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scale = new Vector3f(radius, radius, (float) (source.distance(target)));
        this.displayId = new BlockDisplayId(new BlockDisplayBuilder(midpoint)
                .setMaterial(material)
                .setTransformation(Transformations.lookAlong(scale, Transformations.getDirection(midpoint, target)))
                .setBrightness(15)
                .build()
                .getUniqueId());
    }

    public DirectTicker(@NotNull final TickerId id) {
        this.displayId = new BlockDisplayId(id);
    }

    public TickerId getID() {
        return new TickerId(displayId);
    }

    private Optional<BlockDisplay> getDisplay() {
        return displayId.get();
    }

    @Override
    public void tick() {}

    @Override
    public void remove() {
        getDisplay().ifPresent(BlockDisplay::remove);
    }

    @Override
    public boolean expired() {
        return true;
    }
}

