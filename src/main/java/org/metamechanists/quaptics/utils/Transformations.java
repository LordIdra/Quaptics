package org.metamechanists.quaptics.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Transformations {
    public final Vector3f GENERIC_ROTATION_ANGLES = new Vector3f(-0.955F, 0.785F, 0);

    private final List<BlockFace> AXIS = new ArrayList<>(List.of(
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    ));
    private final List<Vector3f> BLOCK_VERTICES = new ArrayList<>(List.of(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 1),
            new Vector3f(0, 1, 0),
            new Vector3f(0, 1, 1),
            new Vector3f(1, 0, 0),
            new Vector3f(1, 0, 1),
            new Vector3f(1, 1, 0),
            new Vector3f(1, 1, 1)
    ));

    private Vector3f calculateHitboxAdjustmentTranslation(final Matrix4f matrix) {
        // When we rotate a block, the hitbox in X, Y, and Z changes
        // We need to account for this change to center the block
        final List<Vector3f> transformedVertices = BLOCK_VERTICES.stream()
                .map(vertex -> new Vector3f(vertex).mulDirection(matrix)).toList();

        final Vector3f min = new Vector3f();
        final Vector3f max = new Vector3f();

        transformedVertices.forEach(vector -> {
            if (vector.x < min.x) { min.x = vector.x; }
            if (vector.y < min.y) { min.y = vector.y; }
            if (vector.z < min.z) { min.z = vector.z; }

            if (vector.x > max.x) { max.x = vector.x; }
            if (vector.y > max.y) { max.y = vector.y; }
            if (vector.z > max.z) { max.z = vector.z; }
        });

        return max.add(min).div(-2);
    }

    public double yawToCardinalDirection(final float yaw) {
        return -Math.round(yaw / 90.0F) * (Math.PI/2);
    }

    public BlockFace yawToFace(final float yaw) {
        return AXIS.get(Math.round(yaw / 90.0F) & 0x3);
    }

    public @NotNull Vector3f getDisplacement(final Location from, @NotNull final Location to) {
        return to.clone().subtract(from).toVector().toVector3f();
    }

    public Vector3f getDirection(final Location from, final Location to) {
        return getDisplacement(from, to).normalize();
    }

    public Matrix4f none() {
        return new Matrix4f().scale(0);
    }

    public Matrix4f lookAlong(final Vector3f scale, @NotNull final Vector3f direction) {
        final float angleY = (float) Math.atan2(direction.x, direction.z);
        final float angleX = (float) Math.atan2(direction.y, Math.sqrt(direction.x*direction.x + direction.z*direction.z));
        final Matrix4f hitboxMatrix = new Matrix4f()
                .rotateY(angleY)
                .rotateX(-angleX)
                .scale(scale);
        return new Matrix4f()
                .translate(calculateHitboxAdjustmentTranslation(hitboxMatrix))
                .rotateY(angleY)
                .rotateX(-angleX)
                .scale(scale);
    }

    public Matrix4f unadjustedScale(final Vector3f scale) {
        return new Matrix4f().scale(scale);
    }

    public Matrix4f adjustedScale(@NotNull final Vector3f scale) {
        return new Matrix4f().translate(-scale.x/2, -scale.y/2, -scale.z/2).scale(scale);
    }

    public Matrix4f adjustedScaleAndOffset(@NotNull final Vector3f scale, final Vector3f offset) {
        return new Matrix4f().translate(offset).mul(adjustedScale(scale));
    }

    public Matrix4f adjustedRotateAndScale(final Vector3f scale, final Vector3f rotationInRadians) {
        final Matrix4f hitboxMatrix = new Matrix4f()
                .rotateXYZ(rotationInRadians)
                .scale(scale);
        return new Matrix4f()
                .translate(calculateHitboxAdjustmentTranslation(hitboxMatrix))
                .rotateXYZ(rotationInRadians)
                .scale(scale);
    }

    public Matrix4f unadjustedRotateAndScale(final Vector3f scale, final Vector3f rotationInRadians) {
        return new Matrix4f()
                .rotateXYZ(rotationInRadians)
                .scale(scale);
    }
}
