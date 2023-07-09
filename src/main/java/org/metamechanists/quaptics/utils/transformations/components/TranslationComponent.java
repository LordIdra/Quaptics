package org.metamechanists.quaptics.utils.transformations.components;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Represents a translation in X, Y, and Z.
 */
public class TranslationComponent implements TransformationMatrixComponent {
    private final Vector3f translation;

    public TranslationComponent(@NotNull final Vector3f translation) {
        this.translation = translation;
    }

    @Override
    public void apply(@NotNull final Matrix4f matrix) {
        matrix.translate(translation);
    }
}
