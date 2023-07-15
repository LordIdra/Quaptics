package org.metamechanists.quaptics.beams.beam;

import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class LifetimeDirectBeam extends DirectBeam {
    private int lifetime;

    public LifetimeDirectBeam(final Material material, @NotNull final Location source, final Location target, final float thickness, final int lifetime) {
        super(material, source, target, thickness);

        this.lifetime = lifetime;
    }

    @Override
    public void tick() {
        this.lifetime--;
    }

    @Override
    public boolean expired() {
        return lifetime <= 0;
    }
}
