package org.metamechanists.quaptics.utils.transformations;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.transformations.components.RotationComponent;
import org.metamechanists.quaptics.utils.transformations.components.ScaleComponent;
import org.metamechanists.quaptics.utils.transformations.components.TransformationMatrixComponent;
import org.metamechanists.quaptics.utils.transformations.components.TranslationComponent;

import java.util.ArrayDeque;
import java.util.Deque;


/**
 * Builds a transformation matrix by combining {@link TransformationMatrixComponent}s
 */
public class TransformationMatrixBuilder {
    private static final Vector3f BLOCK_DISPLAY_ADJUSTMENT = new Vector3f(-0.5F);

    final Deque<TransformationMatrixComponent> components = new ArrayDeque<>();

    public TransformationMatrixBuilder translate(final Vector3f scale) {
        components.addLast(new TranslationComponent(scale));
        return this;
    }
    public TransformationMatrixBuilder scale(final Vector3f scale) {
        components.addLast(new ScaleComponent(scale));
        return this;
    }
    public TransformationMatrixBuilder rotate(final Vector3f scale) {
        components.addLast(new RotationComponent(scale));
        return this;
    }

    private @NotNull Matrix4f build() {
        // Reverse order is used because JOML applies transformations in reverse order
        // For example new Matrix4f().scale(xyz).rotate(xyz) first rotates and then scales, which is unintuitive and so we want to reverse this behaviour
        final Matrix4f matrix = new Matrix4f();
        while (!components.isEmpty()) {
            components.removeLast().apply(matrix);
        }
        return matrix;
    }

    /**
     * Adjusts the transformation so that the transformation acts on the center of the block display; otherwise it would act on a corner, which is usually less useful
     * @return The matrix representing the transformation formed by all the components
     */
    public @NotNull Matrix4f buildForBlockDisplay() {
        components.addFirst(new TranslationComponent(BLOCK_DISPLAY_ADJUSTMENT));
        return build();
    }
    public @NotNull Matrix4f buildForItemDisplay() {
        return build();
    }
    public @NotNull Matrix4f buildForTextDisplay() {
        return build();
    }
}
