package org.metamechanists.quaptics.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.stream.IntStream;


@UtilityClass
public class Particles {
    public void animatedLine(final Particle particle, @NotNull final Location from, @NotNull final Location to, final int particleCount, final double offset) {
        final Vector3f direction = Transformations.getDirection(from, to);
        final double distance = from.distance(to);
        final float spacing = (float) ((1.0F / (particleCount-1)) * distance);
        final Location currentLocation = from.clone();//.add(Vector.fromJOML(new Vector3f(direction).mul((float) (offset * spacing))));
        final Vector locationIncrement = Vector.fromJOML(new Vector3f(direction).mul(spacing));

        IntStream.range(0, particleCount).forEach(i -> {
            from.getWorld().spawnParticle(particle, currentLocation, 1, 0, 0, 0, 0);
            currentLocation.add(locationIncrement);
        });
    }
}
