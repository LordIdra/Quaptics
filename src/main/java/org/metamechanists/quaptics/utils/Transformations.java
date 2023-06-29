package org.metamechanists.quaptics.utils;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Transformations {
    private static final List<Vector3f> BLOCK_VERTICES = new ArrayList<>();
    static {{
        BLOCK_VERTICES.add(new Vector3f(0, 0, 0));
        BLOCK_VERTICES.add(new Vector3f(0, 0, 1));
        BLOCK_VERTICES.add(new Vector3f(0, 1, 0));
        BLOCK_VERTICES.add(new Vector3f(0, 1, 1));
        BLOCK_VERTICES.add(new Vector3f(1, 0, 0));
        BLOCK_VERTICES.add(new Vector3f(1, 0, 1));
        BLOCK_VERTICES.add(new Vector3f(1, 1, 0));
        BLOCK_VERTICES.add(new Vector3f(1, 1, 1));
    }}

    private static Vector3f calculateHitboxAdjustmentTranslation(Matrix4f matrix) {
        // When we rotate a block, the hitbox in X, Y, and Z changes
        // We need to account for this change to center the block
        final List<Vector3f> transformedVertices = new ArrayList<>();

        for (Vector3f vertex : BLOCK_VERTICES) {
            transformedVertices.add(new Vector3f(vertex).mulDirection(matrix));
        }

        final Vector3f min = new Vector3f();
        final Vector3f max = new Vector3f();

        transformedVertices.forEach(vector -> {
            if (vector.x < min.x) { min.x = vector.x; }
            if (vector.y < min.y) { min.y = vector.y; }
            if (vector.z < min.z) { min.z = vector.z; }

            if (vector.x > max.x) { max.x = vector.x;}
            if (vector.y > max.y) { max.y = vector.y; }
            if (vector.z > max.z) { max.z = vector.z; }
        });

        return max.add(min).div(-2);
    }

    public static double yawToCardinalDirection(float yaw) {
        return -Math.round(yaw / 90F) * (Math.PI/2);
    }

    public static @NotNull Vector3f getDisplacement(Location from, @NotNull Location to) {
        return to.clone().subtract(from).toVector().toVector3f();
    }

    public static Vector3f getDirection(Location from, Location to) {
        return getDisplacement(from, to).normalize();
    }

    public static Matrix4f lookAlong(Vector3f scale, @NotNull Vector3f direction) {
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

    public static Matrix4f unadjustedScale(Vector3f scale) {
        return new Matrix4f().scale(scale);
    }

    public static Matrix4f adjustedScale(@NotNull Vector3f scale) {
        return new Matrix4f().translate(-scale.x/2, -scale.y/2, -scale.z/2).scale(scale);
    }

    public static Matrix4f adjustedRotateAndScale(Vector3f scale, Vector3f rotationInRadians) {
        final Matrix4f hitboxMatrix = new Matrix4f()
                .rotateXYZ(rotationInRadians)
                .scale(scale);
        return new Matrix4f()
                .translate(calculateHitboxAdjustmentTranslation(hitboxMatrix))
                .rotateXYZ(rotationInRadians)
                .scale(scale);
    }

    public static Matrix4f unadjustedRotateAndScale(Vector3f scale, Vector3f rotationInRadians) {
        return new Matrix4f()
                .rotateXYZ(rotationInRadians)
                .scale(scale);
    }
}
