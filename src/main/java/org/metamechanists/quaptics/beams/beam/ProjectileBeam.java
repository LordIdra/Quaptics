package org.metamechanists.quaptics.beams.beam;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.simple.BlockDisplayId;
import org.metamechanists.quaptics.utils.models.components.ModelLine;
import org.metamechanists.quaptics.utils.transformations.TransformationUtils;

import java.util.Optional;

public class ProjectileBeam implements Beam {
    private final Vector3f velocity;
    private final BlockDisplayId displayId;
    private final int lifespanTicks;
    private int ageTicks;

    public ProjectileBeam(final Material material, final Location source, final Location target, final float thickness, final float length, final float speed) {
        final Location midpoint = TransformationUtils.getMidpoint(source, target);
        this.velocity = TransformationUtils.getDisplacement(source, target).normalize().mul(speed);
        this.lifespanTicks = (int) (TransformationUtils.getDisplacement(source, target).length() / speed) + 1;
        this.displayId = new BlockDisplayId(new ModelLine()
                .from(TransformationUtils.getDisplacement(midpoint, source))
                .to(TransformationUtils.getDirection(midpoint, target).mul(length))
                .thickness(thickness)
                .brightness(Utils.BRIGHTNESS_ON)
                .material(material)
                .build(midpoint)
                .getUniqueId());
    }

    private Optional<BlockDisplay> getDisplay() {
        return displayId.get();
    }

    @Override
    public void tick() {
        getDisplay().ifPresent(display -> display.teleport(display.getLocation().add(Vector.fromJOML(velocity))));
        ageTicks++;
    }

    @Override
    public void remove() {
        getDisplay().ifPresent(BlockDisplay::remove);
    }

    @Override
    public boolean expired() {
        return ageTicks > lifespanTicks;
    }
}

