package org.metamechanists.quaptics.implementation.tools.raygun;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.beams.beam.ProjectileBeam;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.storage.QuapticTicker;

public class ModulatedRayGun extends AbstractRayGun {
    public static final Settings RAY_GUN_1_SETTINGS = Settings.builder()
            .chargeCapacity(1000.0)
            .emissionPower(5.0)
            .damage(1)
            .projectileSpeed(3)
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .build();

    public static final SlimefunItemStack RAY_GUN_1 = new SlimefunItemStack(
            "QP_RAY_GUN_1",
            Material.DIAMOND_HORSE_ARMOR,
            "&bRay Gun &3I",
            Lore.buildChargeableLore(RAY_GUN_1_SETTINGS, 0,
                    "&7● &eRight Click &7to fire"));

    public ModulatedRayGun(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    public void fireRayGun(final Player player, final Location source, final Location target, final Vector handToEyeDisplacement) {
        DeprecatedBeamStorage.deprecate(new ProjectileBeam(
                settings.getProjectileMaterial(),
                source,
                target,
                0.095F,
                0.2F,
                settings.getProjectileSpeed() / QuapticTicker.TICKS_PER_SECOND,
                settings.getDamage()));
    }
}
