package org.metamechanists.quaptics.implementation.blocks.consumers.turrets;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.DeprecatedTickerStorage;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;

public class DirectTurret extends Turret {
    private static final float BEAM_RADIUS = 0.095F;

    public DirectTurret(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Settings settings) {
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

        DeprecatedTickerStorage.deprecate(new DirectTicker(
                settings.getProjectileMaterial(),
                location.clone().add(barrelLocation),
                target.getEyeLocation(),
                BEAM_RADIUS));

        target.damage(settings.getDamage());
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.SMOOTH_STONE_SLAB;
    }
}
