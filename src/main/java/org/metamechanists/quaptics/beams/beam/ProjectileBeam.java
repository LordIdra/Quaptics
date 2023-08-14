package org.metamechanists.quaptics.beams.beam;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.simple.BlockDisplayId;
import org.metamechanists.quaptics.utils.models.components.ModelLine;
import org.metamechanists.quaptics.utils.transformations.TransformationUtils;

import java.util.List;
import java.util.Optional;

public class ProjectileBeam implements Beam {
    private final Player player;
    private final Vector velocity;
    private final BlockDisplayId displayId;
    private final float thickness;
    private final double damage;
    private final int lifetimeTicks;
    private int ageTicks;

    public ProjectileBeam(final Player player, final Material material, final Location source, final Location target,
                          final float thickness, final float length, final float speed, final double damage, final int lifetimeTicks) {
        this.player = player;
        this.velocity = Vector.fromJOML(TransformationUtils.getDisplacement(source, target).normalize().mul(speed));
        this.displayId = new BlockDisplayId(new ModelLine()
                .to(TransformationUtils.getDirection(source, target).mul(length))
                .thickness(thickness)
                .brightness(Utils.BRIGHTNESS_ON)
                .material(material)
                .build(source)
                .getUniqueId());
        this.thickness = thickness;
        this.damage = damage;
        this.lifetimeTicks = lifetimeTicks;
    }

    private Optional<BlockDisplay> getDisplay() {
        return displayId.get();
    }

    @Override
    public void tick() {
        getDisplay().ifPresent(display -> {
            display.teleport(display.getLocation().add(velocity));

            final List<Entity> nearbyEntities = display.getNearbyEntities(thickness, thickness, thickness);
            if (nearbyEntities.isEmpty()) {
                return;
            }

            final Optional<Damageable> hitEntity =  nearbyEntities.stream()
                    .filter(Damageable.class::isInstance)
                    .map(Damageable.class::cast)
                    .findFirst();

            hitEntity.ifPresent(entity -> {
                if (Slimefun.getProtectionManager().hasPermission(player, entity.getLocation(), Interaction.ATTACK_ENTITY)) {
                    entity.damage(damage);
                    entity.setVelocity(velocity.clone().normalize().multiply(0.2));
                }
                DeprecatedBeamStorage.remove(this);
            });
        });
        ageTicks++;
    }

    @Override
    public void remove() {
        getDisplay().ifPresent(BlockDisplay::remove);
    }

    @Override
    public boolean expired() {
        return ageTicks > lifetimeTicks;
    }
}

