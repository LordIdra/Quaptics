package org.metamechanists.quaptics.beams.ticker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.BlockDisplayId;
import org.metamechanists.quaptics.utils.id.TickerId;

public class DirectTicker implements DisplayTicker {
    private final BlockDisplayId displayID;

    public DirectTicker(Material material, @NotNull Location source, Location target, float radius) {
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scale = new Vector3f(radius, radius, (float)(source.distance(target)));
        this.displayID = new BlockDisplayId(new BlockDisplayBuilder(midpoint)
                        .setMaterial(material)
                        .setTransformation(Transformations.lookAlong(scale, Transformations.getDirection(midpoint, target)))
                        .setBrightness(15)
                        .build()
                        .getUniqueId());
    }

    public DirectTicker(@NotNull TickerId ID) {
        this.displayID = new BlockDisplayId(ID);
    }

    public TickerId getID() {
        return new TickerId(displayID);
    }

    private BlockDisplay getDisplay() {
        return (BlockDisplay) Bukkit.getEntity(displayID.getUUID());
    }

    @Override
    public void tick() {}

    @Override
    public void remove() {
        if (getDisplay() != null) {
            getDisplay().remove();
        }
    }

    @Override
    public boolean expired() {
        return true;
    }
}

