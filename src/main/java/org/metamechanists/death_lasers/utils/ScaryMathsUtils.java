package org.metamechanists.death_lasers.utils;

import org.bukkit.Location;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class ScaryMathsUtils {
    public static Vector getDisplacement(Location from, Location to) {
        return to.clone().subtract(from).toVector();
    }

    public static Vector getDirection(Location from, Location to) {
        return getDisplacement(from, to).normalize();
    }

    public static float getHorizontalRotation(Location from, Location to) {
        final Vector direction = getDirection(from, to);
        return (float) Math.atan2(direction.getX(), direction.getZ());
    }

    public static float getVerticalRotation(Location from, Location to) {
        final Vector displacement = getDisplacement(from, to);
        final float horizontalRotation = getHorizontalRotation(from, to);
        final Vector directionVectorInOnlyHorizontalPlane = new Vector(0, 0, 1).rotateAroundY(horizontalRotation);
        float verticalRotation = directionVectorInOnlyHorizontalPlane.angle(displacement);
        if (displacement.getY() > 0) {
            verticalRotation = -verticalRotation;
        }
        return verticalRotation;
    }

    public static Transformation createDisplayTransformationType1(Location source, Location target, float scale) {
        // Locate the Display entity (with size scale) at the source, with one of the faces facing the target
        float verticalRotation = ScaryMathsUtils.getVerticalRotation(source, target);
        float horizontalRotation = ScaryMathsUtils.getHorizontalRotation(source, target);
        final Vector offset = new Vector(-scale/2, -scale/2, -scale/2)
                .rotateAroundY(horizontalRotation)
                .rotateAroundAxis(new Vector(Math.cos(horizontalRotation), 0, -Math.sin(horizontalRotation)), verticalRotation)
                .add(new Vector(0, scale, 0));
        return new Transformation(
                new Vector3f((float)offset.getX(), (float)offset.getY(), (float)offset.getZ()),
                new AxisAngle4f(horizontalRotation, 0, 1, 0),
                new Vector3f(scale, scale, scale),
                new AxisAngle4f(verticalRotation, 1, 0, 0));
    }

    public static Transformation createDisplayTransformationType1(Location source, Vector direction, float scale) {
        return createDisplayTransformationType1(source, source.clone().add(direction), scale);
    }

    public static Transformation createDisplayTransformationType2(Location source, float scale) {
        // Orient the block as a regular diamond
        final float rotationAroundAllAxes = (float)Math.PI/4;
        final float offsetOnAllAxes = -(scale/2) * (float)Math.cos(rotationAroundAllAxes);
        final Vector offset = new Vector(offsetOnAllAxes, offsetOnAllAxes, offsetOnAllAxes);
        return new Transformation(
                new Vector3f((float)offset.getX(), (float)offset.getY(), (float)offset.getZ()),
                new AxisAngle4f(rotationAroundAllAxes, 0, 1, 0),
                new Vector3f(scale, scale, scale),
                new AxisAngle4f(rotationAroundAllAxes, 0, 0, 1));
    }
}
