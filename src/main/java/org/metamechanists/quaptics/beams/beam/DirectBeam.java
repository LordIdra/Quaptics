package org.metamechanists.quaptics.beams.beam;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.displaymodellib.models.components.ModelLine;
import org.metamechanists.displaymodellib.transformations.TransformationUtils;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.DirectBeamId;
import org.metamechanists.quaptics.utils.id.simple.BlockDisplayId;

import java.util.Optional;

public class DirectBeam implements Beam {
    private final BlockDisplayId displayId;

    public DirectBeam(final Material material, @NotNull final Location source, final Location target, final float thickness, final double roll) {
        final Location midpoint = TransformationUtils.getMidpoint(source, target);
        this.displayId = new BlockDisplayId(new ModelLine()
                .from(TransformationUtils.getDisplacement(midpoint, source))
                .to(TransformationUtils.getDisplacement(midpoint, target))
                .thickness(thickness)
                .roll(roll)
                .brightness(Utils.BRIGHTNESS_ON)
                .material(material)
                .build(midpoint)
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
    public void setThicknessAndRoll(final @NotNull Location source, final Location target, final float thickness, final double roll) {
        final Location midpoint = TransformationUtils.getMidpoint(source, target);
        getDisplay().ifPresent(display -> display.setTransformationMatrix(new ModelLine()
                .from(TransformationUtils.getDisplacement(midpoint, source))
                .to(TransformationUtils.getDisplacement(midpoint, target))
                .thickness(thickness)
                .roll(roll)
                .getMatrix()));
    }
}

