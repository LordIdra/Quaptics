package org.metamechanists.quaptics.beams.beam;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.simple.BlockDisplayId;
import org.metamechanists.quaptics.utils.id.complex.DirectBeamId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import java.util.Optional;

public class DirectBeam implements Beam {
    private final BlockDisplayId displayId;

    public DirectBeam(final Material material, @NotNull final Location source, final Location target, final float radius) {
        final Location midpoint = source.clone().add(target).multiply(0.5);
        this.displayId = new BlockDisplayId(new BlockDisplayBuilder(midpoint)
                .setMaterial(material)
                .setTransformation(generateTransformation(source, target, radius))
                .setBrightness(15)
                .build()
                .getUniqueId());
    }

    public DirectBeam(@NotNull final DirectBeamId id) {
        this.displayId = new BlockDisplayId(id);
    }

    public DirectBeamId getId() {
        return new DirectBeamId(displayId);
    }

    private Optional<BlockDisplay> getDisplay() {
        return displayId.get();
    }

    private static @NotNull Matrix4f generateTransformation(final @NotNull Location source, final Location target, final float radius) {
        final Vector3f scale = new Vector3f(radius, radius, (float) source.distance(target));
        return new TransformationMatrixBuilder()
                .scale(scale)
                .lookAlong(Transformations.getDirection(source, target))
                .buildForBlockDisplay();
    }

    @Override
    public void tick() {}

    public void deprecate() {
        DeprecatedBeamStorage.deprecate(this);
    }

    @Override
    public void remove() {
        getDisplay().ifPresent(BlockDisplay::remove);
    }

    @Override
    public boolean expired() {
        return true;
    }

    public void setMaterial(final Material material) {
        getDisplay().ifPresent(display -> display.setBlock(material.createBlockData()));
    }

    public void setRadius(final @NotNull Location source, final Location target, final float radius) {
        getDisplay().ifPresent(display -> display.setTransformationMatrix(generateTransformation(source, target, radius)));
    }
}

