package org.metamechanists.quaptics.utils.transformations.components;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RotationComponent implements TransformationMatrixComponent {
    private final Vector3f rotation;

    public RotationComponent(@NotNull final Vector3f rotation) {
        this.rotation = rotation;
    }

    @Override
    public void apply(@NotNull final Matrix4f matrix) {
        matrix.rotateXYZ(rotation);
    }
}
