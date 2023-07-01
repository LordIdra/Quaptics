package org.metamechanists.quaptics.implementation.blocks.consumers.turrets;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.DeprecatedTickerStorage;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.implementation.base.Settings;

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

        final DisplayGroup group = getDisplayGroup(location);
        final Display display = group != null
                ? group.getDisplays().get("barrel")
                : null;

        if (group != null && display != null) {
            display.setTransformationMatrix(getBarrelMatrix(location, target.getEyeLocation()));
        }

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
