package org.metamechanists.quaptics.implementation.blocks.consumers.turrets;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.beams.beam.ProjectileBeam;
import org.metamechanists.quaptics.implementation.blocks.Settings;

import java.util.Set;


public class ModulatedTurret extends Turret {
    public static final Settings TURRET_1_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.55F)
            .minPower(5)
            .range(5)
            .damage(1)
            .projectileSpeed(3)
            .targets(Set.of(SpawnCategory.MONSTER))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.POLISHED_ANDESITE)
            .build();
    public static final Settings TURRET_1_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.55F)
            .minPower(5)
            .range(5)
            .damage(1)
            .projectileSpeed(3)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.POLISHED_ANDESITE)
            .build();
    public static final SlimefunItemStack TURRET_1_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_1_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eI &8(targets hostiles)",
            Lore.create(TURRET_1_HOSTILE_SETTINGS,
                    "&7● Modulated projectiles",
                    "&7● Shoots at nearby entities"));
    public static final SlimefunItemStack TURRET_1_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_1_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eII &8(targets passives)",
            Lore.create(TURRET_1_PASSIVE_SETTINGS,
                    "&7● Modulated projectiles",
                    "&7● Shoots at nearby entities"));

    private final Vector3f projectileSize = new Vector3f(0.095F, 0.095F, 0.20F);

    public ModulatedTurret(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void createProjectile(final Location source, final Location target) {
        DeprecatedBeamStorage.deprecate(new ProjectileBeam(
                settings.getProjectileMaterial(), source, target, projectileSize, settings.getProjectileSpeed() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND));
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.SMOOTH_STONE_SLAB;
    }
}
