package org.metamechanists.quaptics.utils.transformations;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.transformations.components.LookAlongComponent;
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

    private final Deque<TransformationMatrixComponent> components = new ArrayDeque<>();

    /**
     * Represents a translation in X, Y, and Z.
     * @param translation The amount by which to translate in the x, y, and z plane
     */
    public TransformationMatrixBuilder translate(final @NotNull Vector3f translation) {
        components.addLast(new TranslationComponent(translation));
        return this;
    }

    /**
     * Represents a scale transformation in X, Y, and Z.
     * @param scale The amount by which to scale in the x, y, and z plane
     */
    public TransformationMatrixBuilder scale(final @NotNull Vector3f scale) {
        components.addLast(new ScaleComponent(scale));
        return this;
    }

    /**
     * Represents a rotation in X, Y, and Z.
     * @param rotation The amount by which to rotate in the x, y, and z plane
     */
    public TransformationMatrixBuilder rotate(final @NotNull Vector3f rotation) {
        components.addLast(new RotationComponent(rotation));
        return this;
    }

    /**
     * Represents a look-along transformation without any roll. To visualise what this transformation does, imagine a player in-game rotating their head.
     * @param direction The direction along which to look
     */
    public TransformationMatrixBuilder lookAlong(final @NotNull Vector3f direction) {
        components.addLast(new LookAlongComponent(direction));
        return this;
    }
    public TransformationMatrixBuilder lookAlong(final @NotNull Location from, final @NotNull Location to) {
        components.addLast(new LookAlongComponent(from, to));
        return this;
    }
    public TransformationMatrixBuilder lookAlong(final @NotNull BlockFace face) {
        components.addLast(new LookAlongComponent(face.getDirection().toVector3f()));
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
