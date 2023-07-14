package org.metamechanists.quaptics.implementation.blocks.consumers.turrets;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

import java.util.Set;


public class DirectTurret extends Turret {
    public static final Settings TURRET_2_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .connectionRadius(0.55F)
            .minPower(40)
            .minFrequency(1.0)
            .range(7)
            .damage(2)
            .targets(Set.of(SpawnCategory.MONSTER))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.DEEPSLATE_TILES)
            .build();
    public static final Settings TURRET_2_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .connectionRadius(0.55F)
            .minPower(40)
            .minFrequency(6)
            .range(7)
            .damage(2)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.DEEPSLATE_TILES)
            .build();
    public static final SlimefunItemStack TURRET_2_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_2_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eII &8(targets hostiles)",
            Lore.create(TURRET_2_HOSTILE_SETTINGS,
                    "&7● Direct beam",
                    "&7● Shoots at nearby entities"));
    public static final SlimefunItemStack TURRET_2_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_2_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eII &8(targets passives)",
            Lore.create(TURRET_2_PASSIVE_SETTINGS,
                    "&7● Direct beam",
                    "&7● Shoots at nearby entities"));

    private static final float BEAM_RADIUS = 0.095F;

    public DirectTurret(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void createProjectile(final Location source, final Location target) {
        DeprecatedBeamStorage.deprecate(new DirectBeam(settings.getProjectileMaterial(), source, target, BEAM_RADIUS));
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.SMOOTH_STONE_SLAB;
    }
}
