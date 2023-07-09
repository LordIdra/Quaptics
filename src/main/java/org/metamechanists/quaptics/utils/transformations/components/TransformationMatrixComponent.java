package org.metamechanists.quaptics.utils.transformations.components;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;


/**
 * Represents a single component that can be applied in a transformation.
 */
@FunctionalInterface
public interface TransformationMatrixComponent {
    void apply(@NotNull final Matrix4f matrix);
}
