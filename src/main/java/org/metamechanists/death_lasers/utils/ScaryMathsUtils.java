package org.metamechanists.death_lasers.utils;

import org.bukkit.Location;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

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

    //public static Transformation
}
