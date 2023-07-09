package org.metamechanists.quaptics.utils.transformations.components;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class LookAlongComponent extends RotationComponent {
    public LookAlongComponent(@NotNull final Vector3f direction) {
        super(new Vector3f(getAngleX(direction), getAngleY(direction), 0));
    }

    private static float getAngleX(@NotNull final Vector3f direction) {
        return (float) Math.atan2(direction.y, Math.sqrt(direction.x*direction.x + direction.z*direction.z));
    }
    private static float getAngleY(@NotNull final Vector3f direction) {
        return (float) Math.atan2(direction.x, direction.z);
    }
}
