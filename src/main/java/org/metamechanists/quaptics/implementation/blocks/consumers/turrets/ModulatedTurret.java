package org.metamechanists.quaptics.implementation.blocks.consumers.turrets;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.beams.DeprecatedTickerStorage;
import org.metamechanists.quaptics.beams.ticker.IntervalVelocityTicker;

public class ModulatedTurret extends Turret {
    private final Vector3f projectileSize = new Vector3f(0.095F, 0.095F, 0.20F);

    public ModulatedTurret(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Settings settings) {
        super(group, item, recipeType, recipe, settings);
    }

    @Override
    protected void shoot(Location location) {
        final LivingEntity target = getTarget(location);

        if (target == null || target.isDead() || location.toCenterLocation().distance(target.getLocation()) > settings.getRange()) {
            clearTarget(location);
            return;
        }

        getDisplayGroup(location).getDisplays().get("barrel").setTransformationMatrix(getBarrelMatrix(location, target.getEyeLocation()));

        DeprecatedTickerStorage.deprecate(new IntervalVelocityTicker(
                settings.getProjectileMaterial(),
                location.clone().add(barrelLocation),
                target.getEyeLocation(),
                projectileSize,
                settings.getProjectileSpeed()));

        target.damage(settings.getDamage());
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.SMOOTH_STONE_SLAB;
    }
}
