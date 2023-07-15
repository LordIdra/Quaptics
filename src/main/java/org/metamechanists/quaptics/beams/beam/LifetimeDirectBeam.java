package org.metamechanists.quaptics.beams.beam;

import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class LifetimeDirectBeam extends DirectBeam {
    private int lifetime;

    public LifetimeDirectBeam(Material material, @NotNull Location source, Location target, float thickness, int lifetime) {
        super(material, source, target, thickness);

        this.lifetime = lifetime;
    }

    @Override
    public void tick() {
        this.lifetime--;
    }

    @Override
    public boolean expired() {
        return this.lifetime > 0;
    }
}
