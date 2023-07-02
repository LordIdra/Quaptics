package org.metamechanists.quaptics.implementation.blocks.consumers.turrets;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.beams.DeprecatedTickerStorage;
import org.metamechanists.quaptics.beams.ticker.IntervalVelocityTicker;
import org.metamechanists.quaptics.implementation.blocks.Settings;

public class ModulatedTurret extends Turret {
    private final Vector3f projectileSize = new Vector3f(0.095F, 0.095F, 0.20F);

    public ModulatedTurret(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(group, item, recipeType, recipe, settings);
    }

    @Override
    protected void createProjectile(final Location source, final Location target) {
        DeprecatedTickerStorage.deprecate(new IntervalVelocityTicker(
                settings.getProjectileMaterial(), source, target, projectileSize, settings.getProjectileSpeed()));
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.SMOOTH_STONE_SLAB;
    }
}
