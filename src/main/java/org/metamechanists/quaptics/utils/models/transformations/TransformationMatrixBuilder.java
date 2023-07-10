package org.metamechanists.quaptics.utils.models.transformations;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.models.transformations.components.LookAlongComponent;
import org.metamechanists.quaptics.utils.models.transformations.components.RotationComponent;
import org.metamechanists.quaptics.utils.models.transformations.components.ScaleComponent;
import org.metamechanists.quaptics.utils.models.transformations.components.TransformationMatrixComponent;
import org.metamechanists.quaptics.utils.models.transformations.components.TranslationComponent;

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
     */
    public TransformationMatrixBuilder translate(final @NotNull Vector3f translation) {
        components.addLast(new TranslationComponent(translation));
        return this;
    }
    /**
     * Represents a translation in X, Y, and Z.
     */
    public TransformationMatrixBuilder translate(final float x, final float y, final float z) {
        translate(new Vector3f(x, y, z));
        return this;
    }

    /**
     * Represents a scale transformation in X, Y, and Z.
     */
    public TransformationMatrixBuilder scale(final @NotNull Vector3f scale) {
        components.addLast(new ScaleComponent(scale));
        return this;
    }
    /**
     * Represents a scale transformation in X, Y, and Z.
     */
    public TransformationMatrixBuilder scale(final float x, final float y, final float z) {
        scale(new Vector3f(x, y, z));
        return this;
    }

    /**
     * Represents a rotation in X, Y, and Z (angles in radians)
     */
    public TransformationMatrixBuilder rotate(final @NotNull Vector3f rotation) {
        components.addLast(new RotationComponent(rotation));
        return this;
    }
    /**
     * Represents a rotation in X, Y, and Z (angles in radians)
     */
    public TransformationMatrixBuilder rotate(final float x, final float y, final float z) {
        rotate(new Vector3f(x, y, z));
        return this;
    }

    /**
     * Represents a look-along transformation without any roll. To visualise what this transformation does, imagine a player in-game rotating their head.
     */
    public TransformationMatrixBuilder lookAlong(final @NotNull Vector3f direction) {
        components.addLast(new LookAlongComponent(direction));
        return this;
    }
    /**
     * Represents a look-along transformation without any roll. To visualise what this transformation does, imagine a player in-game rotating their head.
     */
    public TransformationMatrixBuilder lookAlong(final @NotNull Location from, final @NotNull Location to) {
        components.addLast(new LookAlongComponent(from, to));
        return this;
    }
    /**
     * Represents a look-along transformation without any roll. To visualise what this transformation does, imagine a player in-game rotating their head.
     */
    public TransformationMatrixBuilder lookAlong(final @NotNull BlockFace face) {
        components.addLast(new LookAlongComponent(face.getDirection().toVector3f()));
        return this;
    }

    private @NotNull Matrix4f build() {
        // Reverse order is used because JOML applies transformations in reverse order
        // For example new Matrix4f().scale(xyz).rotate(xyz) first rotates and then scales, which is unintuitive and so we want to reverse this behaviour
        final Matrix4f matrix = new Matrix4f();
        while (!components.isEmpty()) {
            components.removeFirst().apply(matrix);
        }
        return matrix;
    }

    /**
     * Adjusts the transformation so that the transformation acts on the center of the block display; otherwise it would act on a corner, which is usually less useful
     * @return The matrix representing the transformation formed by all the components
     */
    public @NotNull Matrix4f buildForBlockDisplay() {
        components.addLast(new TranslationComponent(BLOCK_DISPLAY_ADJUSTMENT));
        return build();
    }
    public @NotNull Matrix4f buildForItemDisplay() {
        return build();
    }
    public @NotNull Matrix4f buildForTextDisplay() {
        return build();
    }
}
