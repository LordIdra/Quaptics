package org.metamechanists.quaptics.implementation.tools.raygun;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.beams.beam.ProjectileBeam;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticTicker;

public class ModulatedRayGun extends AbstractRayGun {
    public static final Settings RAY_GUN_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .chargeCapacity(1000.0)
            .outputPower(5.0)
            .range(56)
            .damage(1.5)
            .projectileSpeed(12)
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .build();
    public static final Settings RAY_GUN_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .chargeCapacity(10000.0)
            .outputPower(40.0)
            .range(56)
            .damage(3)
            .projectileSpeed(30)
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .build();

    public static final SlimefunItemStack RAY_GUN_1 = new SlimefunItemStack(
            "QP_RAY_GUN_1",
            Material.LEATHER_HORSE_ARMOR,
            "&bRay Gun &3I",
            Lore.buildChargeableLore(RAY_GUN_1_SETTINGS, 0,
                    "&7● &eRight Click &7to fire"));
    public static final SlimefunItemStack RAY_GUN_2 = new SlimefunItemStack(
            "QP_RAY_GUN_2",
            Material.IRON_HORSE_ARMOR,
            "&bRay Gun &3II",
            Lore.buildChargeableLore(RAY_GUN_2_SETTINGS, 0,
                    "&7● &eRight Click &7to fire"));

    public ModulatedRayGun(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    public void fireRayGun(final Player player, final Location eyeLocation, final Location handLocation, final Location target) {
        DeprecatedBeamStorage.deprecate(new ProjectileBeam(
                player,
                settings.getProjectileMaterial(),
                handLocation,
                target,
                0.095F,
                0.2F,
                settings.getProjectileSpeed() * QuapticTicker.INTERVAL_TICKS_2 / QuapticTicker.TICKS_PER_SECOND,
                settings.getDamage(),
                (int) (20 * settings.getRange() / settings.getProjectileSpeed())));
    }
}
