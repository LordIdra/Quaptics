package org.metamechanists.quaptics.utils.models.transformations.components;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.models.transformations.TransformationUtils;


/**
 * LookAlong is slightly more complex than the other components. Bukkit's coordinate system does not work very well with regular JOML vectors in the context of LookAlong.
 * This means we cannot use JOML's LookAlong function, and instead must create our own by calculating the angles manually.
 */
public class LookAlongComponent implements TransformationMatrixComponent {
    private final Vector3f direction;

    public LookAlongComponent(@NotNull final Vector3f direction) {
        this.direction = direction;
    }

    public LookAlongComponent(@NotNull final Location from, @NotNull final Location to) {
        this.direction = TransformationUtils.getDirection(from, to);
    }

    private float getAngleX() {
        return (float) -Math.atan2(direction.y, Math.sqrt(direction.x*direction.x + direction.z*direction.z));
    }
    private float getAngleY() {
        return (float) Math.atan2(direction.x, direction.z);
    }

    @Override
    public void apply(@NotNull final Matrix4f matrix) {
        matrix.rotateY(getAngleY()).rotateX(getAngleX());
    }
}
