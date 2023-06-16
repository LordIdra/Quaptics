package org.metamechanists.death_lasers.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DisplayUtils {
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

    public static Vector getDisplacement(Location from, Location to) {
        return to.clone().subtract(from).toVector();
    }

    private static Vector getDirection(Location from, Location to) {
        return getDisplacement(from, to).normalize();
    }

    private static float getHorizontalRotation(Location from, Location to) {
        final Vector direction = getDirection(from, to);
        return (float) Math.atan2(direction.getX(), direction.getZ());
    }

    private static float getVerticalRotation(Location from, Location to) {
        final Vector displacement = getDisplacement(from, to);
        final float horizontalRotation = getHorizontalRotation(from, to);
        final Vector directionVectorInOnlyHorizontalPlane = new Vector(0, 0, 1).rotateAroundY(horizontalRotation);
        float verticalRotation = directionVectorInOnlyHorizontalPlane.angle(displacement);
        if (displacement.getY() > 0) {
            verticalRotation = -verticalRotation;
        }
        return verticalRotation;
    }

    private static Vector3f calculateHitboxAdjustmentTranslation(Matrix4f matrix) {
        // When we rotate a block, the hitbox in X, Y, and Z changes
        // We need to account for this change to center the block
        final List<Vector3f> transformedVertices = new ArrayList<>();
        BLOCK_VERTICES.forEach(vertex -> transformedVertices.add(vertex.mulTransposeDirection(matrix)));

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

        final Vector3f hitbox = max.sub(min);
        return hitbox.div(-2);
    }

    public static Matrix4f faceTargetTransformation(Location from, Location to, float scale) {
        // Rotate the display entity so that one of the blockfaces face 'to'
        //float verticalRotation = DisplayUtils.getVerticalRotation(from, to);
        //float horizontalRotation = DisplayUtils.getHorizontalRotation(from, to);

        float rotationX = new Vector3f(0, (float)from.y(), (float)from.z()).angle(new Vector3f(0, (float)to.y(), (float)to.z()));
        float rotationY = new Vector3f((float)from.x(), 0, (float)from.z()).angle(new Vector3f((float)to.x(), 0, (float)to.z()));
        float rotationZ = new Vector3f((float)from.x(), (float)from.y(), 0).angle(new Vector3f((float)to.x(), (float)to.y(), 0));

        return rotationTransformation(
                new Vector3f(scale, scale, scale),
                new Vector3f(rotationX, rotationY, rotationZ));
        //final Vector offset = new Vector(-scale/2, -scale/2, -scale/2)
        //        .rotateAroundY(horizontalRotation)
        //        .rotateAroundAxis(new Vector(Math.cos(horizontalRotation), 0, -Math.sin(horizontalRotation)), verticalRotation)
        //        .add(new Vector(0, scale, 0));
        //return new Transformation(
        //        new Vector3f((float)offset.getX(), (float)offset.getY(), (float)offset.getZ()),
        //        new AxisAngle4f(horizontalRotation, 0, 1, 0),
        //        new Vector3f(scale, scale, scale),
        //        new AxisAngle4f(verticalRotation, 1, 0, 0));
    }

    public static Matrix4f rotationTransformation(Vector3f scale, Vector3f rotationInRadians) {
        // - Translate the block so that the center is where its location is set to
        // - Scale the block by scale
        // - Rotate the block by rotationInDegrees
        Matrix4f matrix = new Matrix4f();
        matrix = matrix.rotateXYZ(rotationInRadians);
        matrix = matrix.scale(scale);
        matrix = matrix.translate(calculateHitboxAdjustmentTranslation(matrix));
        return matrix;
    }

    public static BlockDisplay spawnBlockDisplay(Location location, Material material, Matrix4f transformation) {
        final BlockDisplay display = location.getWorld().spawn(location, BlockDisplay.class);
        display.setBlock(material.createBlockData());
        display.setTransformationMatrix(transformation);
        return display;
    }
}
