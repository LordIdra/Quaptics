package org.metamechanists.quaptics.utils.transformations.components;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class LookAlongComponent implements TransformationMatrixComponent {
    private final Vector3f direction;
    public LookAlongComponent(@NotNull final Vector3f direction) {
        this.direction = direction;
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
