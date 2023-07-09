package org.metamechanists.quaptics.utils.transformations;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TransformationUtils {
    public final Vector3f PRISM_ROTATION = new Vector3f(-0.955F, 0.785F, 0);
    private final List<BlockFace> AXIS = new ArrayList<>(List.of(
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    ));

    @SuppressWarnings("MagicNumber")
    public double yawToCardinalDirection(final float yaw) {
        return -Math.round(yaw / 90.0F) * (Math.PI/2);
    }
    @SuppressWarnings("MagicNumber")
    public BlockFace yawToFace(final float yaw) {
        return AXIS.get(Math.round(yaw / 90.0F) & 0x3);
    }

    public @NotNull Vector3f getDisplacement(final Location from, @NotNull final Location to) {
        return to.clone().subtract(from).toVector().toVector3f();
    }
    public Vector3f getDirection(final Location from, final Location to) {
        return getDisplacement(from, to).normalize();
    }
}
