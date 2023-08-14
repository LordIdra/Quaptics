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
import org.metamechanists.quaptics.beams.beam.LifetimeDirectBeam;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

import java.util.Set;


public class DirectTurret extends Turret {
    public static final Settings TURRET_3_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .minPower(210)
            .minFrequency(200)
            .projectileMaterial(FrequencyColor.getMaterial(200))
            .range(10)
            .damage(4)
            .targets(Set.of(SpawnCategory.MONSTER))
            .build();
    public static final Settings TURRET_3_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .minPower(210)
            .minFrequency(200)
            .projectileMaterial(FrequencyColor.getMaterial(200))
            .range(10)
            .damage(4)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .build();
    public static final Settings TURRET_4_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .minPower(1400)
            .minFrequency(6000)
            .projectileMaterial(FrequencyColor.getMaterial(6000))
            .range(12)
            .damage(7)
            .targets(Set.of(SpawnCategory.MONSTER))
            .build();
    public static final Settings TURRET_4_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .minPower(1400)
            .minFrequency(6000)
            .projectileMaterial(FrequencyColor.getMaterial(6000))
            .range(12)
            .damage(7)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .build();

    public static final SlimefunItemStack TURRET_3_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_3_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&7Turret &fIII &8(targets hostiles)",
            Lore.create(TURRET_3_HOSTILE_SETTINGS,
                    "&7● Shoots at nearby entities"));
    public static final SlimefunItemStack TURRET_3_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_3_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&7Turret &fIII &8(targets passives)",
            Lore.create(TURRET_3_PASSIVE_SETTINGS,
                    "&7● Shoots at nearby entities"));
    public static final SlimefunItemStack TURRET_4_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_4_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&7Turret &fIV &8(targets hostiles)",
            Lore.create(TURRET_4_HOSTILE_SETTINGS,
                    "&7● Shoots at nearby entities"));
    public static final SlimefunItemStack TURRET_4_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_4_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&7Turret &fIV &8(targets passives)",
            Lore.create(TURRET_4_PASSIVE_SETTINGS,
                    "&7● Shoots at nearby entities"));

    private static final float BEAM_RADIUS = 0.095F;
    private static final int BEAM_LIFETIME_TICKS = 3;

    public DirectTurret(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected boolean shouldDamage() {
        return true;
    }

    @Override
    protected void createProjectile(@NotNull final Player player, @NotNull final Location source, final Location target) {
        DeprecatedBeamStorage.deprecate(new LifetimeDirectBeam(settings.getProjectileMaterial(), source.toCenterLocation(), target, BEAM_RADIUS, 0, BEAM_LIFETIME_TICKS));
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.SMOOTH_STONE_SLAB;
    }
}
