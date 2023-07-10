package org.metamechanists.quaptics.utils.models.components;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;


@SuppressWarnings("unused")
public class ModelLine implements ModelComponent {
    private final BlockDisplayBuilder main = new BlockDisplayBuilder();

    private Vector3f from = new Vector3f();
    private Vector3f to = new Vector3f();
    private float thickness;
    private float extraLength;

    /**
     * @param from The start point of the line
     */
    public ModelLine from(final Vector3f from) {
        this.from = from;
        return this;
    }
    /**
     * @param from The end point of the line
     */
    public ModelLine to(final Vector3f to) {
        this.to = to;
        return this;
    }
    /**
     * @param from How thick the line is from one side to the other
     */
    public ModelLine thickness(final float thickness) {
        this.thickness = thickness;
        return this;
    }
    /**
     * @param from How much further than the from/to locations the line should extend (each end has extraLength/2 added to it)
     */
    public ModelLine extraLength(final float extraLength) {
        this.extraLength = extraLength;
        return this;
    }

    public ModelLine material(@NotNull final Material material) {
        main.material(material);
        return this;
    }
    public ModelLine brightness(final int blockBrightness) {
        main.brightness(blockBrightness);
        return this;
    }
    public ModelLine glow(@NotNull final Color color) {
        main.glow(color);
        return this;
    }

    public Matrix4f getMatrix() {
        return new TransformationMatrixBuilder()
                .lookAlong(from, to)
                .scale(new Vector3f(thickness, thickness, from.distance(to) + extraLength))
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