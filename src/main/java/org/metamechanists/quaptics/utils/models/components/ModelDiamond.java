package org.metamechanists.quaptics.utils.models.components;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;


/**
 * Represents a diamond (no not that kind of diamond... this is a rotated cube)
 */
@SuppressWarnings("unused")
public class ModelDiamond implements ModelComponent {
    public static final Vector3d ROTATION = new Vector3d(-0.955, 0.785, 0);

    private final BlockDisplayBuilder main = new BlockDisplayBuilder();

    private Vector3f location = new Vector3f();
    private Vector3f size = new Vector3f();

    /**
     * @param location The center of the cuboid
     */
    public ModelDiamond location(@NotNull final Vector3f location) {
        this.location = location;
        return this;
    }
    /**
     * @param size The size of the cuboid (ie: the distance from one side to the other) on each axis
     */
    public ModelDiamond size(final float size) {
        // The scale() function takes the scale as the side from one face to the next
        // But we actually want the size to be inputted as the distance from one corner to the opposite corner
        // We can accomplish this with basic pythagoras
        this.size = new Vector3f((float) (2 * Math.sqrt(2 * Math.pow(size/2, 2))));
        return this;
    }

    public ModelDiamond material(@NotNull final Material material) {
        main.material(material);
        return this;
    }
    public ModelDiamond brightness(final int blockBrightness) {
        main.brightness(blockBrightness);
        return this;
    }
    public ModelDiamond glow(@NotNull final Color color) {
        main.glow(color);
        return this;
    }

    public Matrix4f getMatrix() {
        return new TransformationMatrixBuilder()
                .translate(location)
                .rotate(ROTATION)
                .scale(size)
                .buildForBlockDisplay();
    }
    @Override
    public BlockDisplay build(@NotNull final Location origin) {
        return main.transformation(getMatrix()).build(origin);
    }
    @Override
    public BlockDisplay build(@NotNull final Block block) {
        return build(block.getLocation());
    }
}
