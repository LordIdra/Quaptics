package org.metamechanists.death_lasers.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DisplayUtils {
    private static final Vector3f UP_VECTOR = new Vector3f(0, 1, 0);
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

    public static Vector getDirection(Location from, Location to) {
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
        if (displacement.getY() > 0) { verticalRotation *= -1; }
        return verticalRotation;
    }

    public static Matrix4f faceTargetTransformation(Location from, Location to, Vector3f scale) {
        // Rotate the display entity so that one of the blockfaces face 'to'
        //final float verticalRotation = DisplayUtils.getVerticalRotation(from, to);
        //final float horizontalRotation = DisplayUtils.getHorizontalRotation(from, to);

        //final Vector3f offset = new Vector3f(-scale.x/2, -scale.y/2, -scale.z/2)
        //        .rotateY(horizontalRotation)
        //        .rotateAxis(verticalRotation, (float)Math.cos(horizontalRotation), 0, -(float)Math.sin(horizontalRotation));
        //return new Transformation(
        //        offset,
        //        new AxisAngle4f(horizontalRotation, 0, 1, 0),
        //        scale,
        //        new AxisAngle4f(verticalRotation, 1, 0, 0));

        //final float verticalRotation = DisplayUtils.getVerticalRotation(from, to);
        //final float horizontalRotation = DisplayUtils.getHorizontalRotation(from, to);

        // -z not fine
        //

        final Vector3f direction = getDirection(from, to).toVector3f();
        return new Matrix4f()
                //.translate(new Vector3f(scale).div(-2))   
                .scale(scale)
                .lookAtLH(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0).add(direction), UP_VECTOR);

    }

    public static Matrix4f simpleScaleTransformation(Vector3f scale) {
        return new Matrix4f().translate(-scale.x/2, -scale.y/2, -scale.z/2).scale(scale);
    }

    private static Vector3f calculateHitboxAdjustmentTranslation(Vector3f scale, Vector3f rotationInRadians) {
        // When we rotate a block, the hitbox in X, Y, and Z changes
        // We need to account for this change to center the block
        final List<Vector3f> transformedVertices = new ArrayList<>();

        final Matrix4f matrix = new Matrix4f().rotateXYZ(rotationInRadians).scale(scale);

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

    public static Matrix4f rotationTransformation(Vector3f scale, Vector3f rotationInRadians) {
        // - Translate the block so that the center is where its location is set to
        // - Scale the block by scale
        // - Rotate the block by rotationInDegrees
        return new Matrix4f()
                .translate(calculateHitboxAdjustmentTranslation(scale, rotationInRadians))
                .rotateXYZ(rotationInRadians)
                .scale(scale);
    }

    public static BlockDisplay spawnBlockDisplay(Location location, Material material, Matrix4f transformation) {
        final BlockDisplay display = location.getWorld().spawn(location, BlockDisplay.class);
        display.setBlock(material.createBlockData());
        display.setTransformationMatrix(transformation);
        display.setDisplayWidth(0);
        display.setDisplayHeight(0);
        return display;
    }

    public static BlockDisplay spawnBlockDisplay(Location location, Material material, Transformation transformation) {
        final BlockDisplay display = location.getWorld().spawn(location, BlockDisplay.class);
        display.setBlock(material.createBlockData());
        display.setTransformation(transformation);
        display.setDisplayWidth(0);
        display.setDisplayHeight(0);
        return display;
    }

    public static Interaction spawnInteraction(Location location, float width, float height) {
        final Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionWidth(width);
        interaction.setInteractionHeight(height);
        return interaction;
    }
}
