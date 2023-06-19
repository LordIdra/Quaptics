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

    public static Matrix4f faceTargetTransformation(Location from, Location to, Vector3f scale) {
        final Vector3f direction = getDirection(from, to).toVector3f();
        direction.mul(-1);

        float angleY = (float) Math.atan2(direction.x, direction.z);
        float angleX = (float) Math.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z));

        Matrix4f rotationYMatrix = new Matrix4f().rotateY(angleY);
        Matrix4f rotationXMatrix = new Matrix4f().rotateX(-angleX);

        return new Matrix4f()
                .translate(calculateHitboxAdjustmentTranslation(scale, new Matrix4f().mul(rotationYMatrix).mul(rotationXMatrix)))
                .mul(rotationYMatrix)
                .mul(rotationXMatrix)
                .scale(scale);
    }

    public static Matrix4f simpleScaleTransformation(Vector3f scale) {
        return new Matrix4f().translate(-scale.x/2, -scale.y/2, -scale.z/2).scale(scale);
    }

    private static Vector3f calculateHitboxAdjustmentTranslation(Vector3f scale, Matrix4f matrix) {
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

    public static Matrix4f rotationTransformation(Vector3f scale, Vector3f rotationInRadians) {
        // - Translate the block so that the center is where its location is set to
        // - Scale the block by scale
        // - Rotate the block by rotationInDegrees
        final Matrix4f hitboxMatrix = new Matrix4f().rotateXYZ(rotationInRadians).scale(scale);
        return new Matrix4f()
                .translate(calculateHitboxAdjustmentTranslation(scale, hitboxMatrix))
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
