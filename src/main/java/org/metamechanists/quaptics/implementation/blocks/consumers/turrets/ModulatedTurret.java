package org.metamechanists.quaptics.implementation.blocks.consumers.turrets;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.beams.FrequencyColor;
import org.metamechanists.quaptics.beams.beam.ProjectileBeam;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticTicker;

import java.util.Set;


public class ModulatedTurret extends Turret {
    public static final Settings TURRET_1_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .minPower(3)
            .range(6)
            .damage(1.5)
            .projectileSpeed(5)
            .targets(Set.of(SpawnCategory.MONSTER))
            .projectileMaterial(FrequencyColor.getMaterial(0))
            .build();
    public static final Settings TURRET_1_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .minPower(3)
            .range(6)
            .damage(1.5)
            .projectileSpeed(5)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .projectileMaterial(FrequencyColor.getMaterial(0))
            .build();
    public static final Settings TURRET_2_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .minPower(25)
            .minFrequency(4)
            .projectileMaterial(FrequencyColor.getMaterial(4))
            .range(8)
            .damage(2.5)
            .projectileSpeed(8)
            .targets(Set.of(SpawnCategory.MONSTER))
            .build();
    public static final Settings TURRET_2_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .minPower(25)
            .minFrequency(4)
            .projectileMaterial(FrequencyColor.getMaterial(4))
            .range(8)
            .damage(2.5)
            .projectileSpeed(8)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .build();

    public static final SlimefunItemStack TURRET_1_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_1_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&7Turret &fI &8(targets hostiles)",
            Lore.create(TURRET_1_HOSTILE_SETTINGS,
                    "&7● Shoots at nearby entities"));
    public static final SlimefunItemStack TURRET_1_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_1_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&7Turret &fI &8(targets passives)",
            Lore.create(TURRET_1_PASSIVE_SETTINGS,
                    "&7● Shoots at nearby entities"));
    public static final SlimefunItemStack TURRET_2_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_2_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&7Turret &fII &8(targets hostiles)",
            Lore.create(TURRET_2_HOSTILE_SETTINGS,
                    "&7● Shoots at nearby entities"));
    public static final SlimefunItemStack TURRET_2_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_2_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&7Turret &fII &8(targets passives)",
            Lore.create(TURRET_2_PASSIVE_SETTINGS,
                    "&7● Shoots at nearby entities"));

    public ModulatedTurret(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected boolean shouldDamage() {
        return false;
    }

    @Override
    protected void createProjectile(@NotNull final Player player, @NotNull final Location source, final Location target) {
        DeprecatedBeamStorage.deprecate(new ProjectileBeam(
                player,
                settings.getProjectileMaterial(),
                source.toCenterLocation(),
                target,
                0.095F,
                0.2F,
                settings.getProjectileSpeed() * QuapticTicker.INTERVAL_TICKS_2 / QuapticTicker.TICKS_PER_SECOND,
                settings.getDamage(),
                (int) (20 * settings.getRange() / settings.getProjectileSpeed())));
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.SMOOTH_STONE_SLAB;
    }
}
