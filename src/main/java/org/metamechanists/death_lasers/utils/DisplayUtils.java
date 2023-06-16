package org.metamechanists.death_lasers.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class DisplayUtils {
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

    public static Matrix4f faceTargetTransformation(Location from, Location to, float scale) {
        // Rotate the display entity so that one of the blockfaces face 'to'
        //float verticalRotation = DisplayUtils.getVerticalRotation(from, to);
        //float horizontalRotation = DisplayUtils.getHorizontalRotation(from, to);

        float rotationX = new Vector3f((float)from.x(), 0, 0).angle(new Vector3f((float)to.x(), 0, 0));
        float rotationY = new Vector3f(0, (float)from.y(), 0).angle(new Vector3f(0, (float)from.y(), 0));
        float rotationZ = new Vector3f(0, 0, (float)from.z()).angle(new Vector3f(0, 0, (float)from.z()));


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
        return new Matrix4f()
                .translate(-scale.x/2, scale.x/2, -scale.z/2)
                .scale(scale)
                .rotateXYZ(rotationInRadians);
    }

    public static BlockDisplay spawnBlockDisplay(Location location, Material material, Matrix4f transformation) {
        final BlockDisplay display = location.getWorld().spawn(location, BlockDisplay.class);
        display.setBlock(material.createBlockData());
        display.setTransformationMatrix(transformation);
        return display;
    }
}
