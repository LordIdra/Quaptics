package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

import java.util.Set;

@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Primitive {
    public final Settings SOLAR_CONCENTRATOR_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.45F)
            .connectionRadius(0.45F)
            .emissionPower(1)
            .build();

    public final Settings LENS_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.24F)
            .connectionRadius(0.48F)
            .powerLoss(0.1)
            .build();

    public final Settings COMBINER_1_2_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.2)
            .connections(2)
            .build();

    public final Settings SPLITTER_1_2_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.2)
            .connections(2)
            .build();

    public final Settings CAPACITOR_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .capacity(200)
            .emissionPower(3)
            .powerLoss(0.25)
            .build();

    public final Settings CHARGER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.6F)
            .build();

    public final Settings TURRET_1_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.55F)
            .connectionRadius(0.55F)
            .minPower(5)
            .range(5)
            .damage(1)
            .projectileSpeed(3)
            .targets(Set.of(SpawnCategory.MONSTER))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.POLISHED_ANDESITE)
            .build();

    public final Settings TURRET_1_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.55F)
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

    public final Settings MULTIBLOCK_CLICKER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();

    public final SlimefunItemStack SOLAR_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_1",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bI",
            Lore.create(SOLAR_CONCENTRATOR_1_SETTINGS,
                    "&7● Only works during the day",
                    "&7● Concentrates sunlight into a quaptic ray"));

    public final SlimefunItemStack LENS_1 = new SlimefunItemStack(
            "QP_LENS_1",
            Material.GLASS,
            "&9Lens &bI",
            Lore.create(LENS_1_SETTINGS,
                    "&7● Redirects a quaptic ray"));

    public final SlimefunItemStack COMBINER_1_2 = new SlimefunItemStack(
            "QP_COMBINER_1_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eI &8(2 connections)",
            Lore.create(COMBINER_1_2_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public final SlimefunItemStack SPLITTER_1_2 = new SlimefunItemStack(
            "QP_SPLITTER_1_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eI &8(2 connections)",
            Lore.create(SPLITTER_1_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public final SlimefunItemStack CHARGER_1 = new SlimefunItemStack(
            "QP_CHARGER_1",
            Material.LIGHT_BLUE_STAINED_GLASS,
            "&bCharger &3I",
            Lore.create(CHARGER_1_SETTINGS,
                    "&7● Charges item with Quaptic Energy Units",
                    "&7● &eRight Click &7an item to place",
                    "&7● &eRight Click &7again to retrieve"));

    public final SlimefunItemStack CAPACITOR_1 = new SlimefunItemStack(
            "QP_CAPACITOR_1",
            Material.LIGHT_BLUE_CONCRETE,
            "&3Capacitor &bI",
            Lore.create(CAPACITOR_1_SETTINGS,
                    "&7● Stores charge",
                    "&7● Outputs at a constant power"));

    public final SlimefunItemStack TURRET_1_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_1_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eI &8(targets hostiles)",
            Lore.create(TURRET_1_HOSTILE_SETTINGS,
                    "&7● Modulated projectiles",
                    "&7● Shoots at nearby entities"));

    public final SlimefunItemStack TURRET_1_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_1_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eII &8(targets passives)",
            Lore.create(TURRET_1_PASSIVE_SETTINGS,
                    "&7● Modulated projectiles",
                    "&7● Shoots at nearby entities"));

    public final SlimefunItemStack MULTIBLOCK_CLICKER_1 = new SlimefunItemStack(
            "QP_MULTIBLOCK_CLICKER_1",
            Material.DISPENSER,
            "&6Multiblock Clicker &eI",
            Lore.create(MULTIBLOCK_CLICKER_1_SETTINGS,
                    "&7● Automatically clicks the attached multiblock",
                    "&7● Place facing the block you'd usually click to use the multiblock",
                    "&7● &eRight Click &7to enable/disable"));

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(
                Groups.PRIMITIVE,
                SOLAR_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                SOLAR_CONCENTRATOR_1_SETTINGS).register(addon);

        new Lens(
                Groups.PRIMITIVE,
                LENS_1,
                RecipeType.NULL,
                new ItemStack[]{},
                LENS_1_SETTINGS).register(addon);

        new Combiner(
                Groups.PRIMITIVE,
                COMBINER_1_2,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_1_2_SETTINGS).register(addon);

        new Splitter(
                Groups.PRIMITIVE,
                SPLITTER_1_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_1_2_SETTINGS).register(addon);

        new Charger(
                Groups.PRIMITIVE,
                CHARGER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                CHARGER_1_SETTINGS).register(addon);

        new Capacitor(
                Groups.PRIMITIVE,
                CAPACITOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                CAPACITOR_1_SETTINGS).register(addon);

        new ModulatedTurret(
                Groups.PRIMITIVE,
                TURRET_1_HOSTILE,
                RecipeType.NULL,
                new ItemStack[]{},
                TURRET_1_HOSTILE_SETTINGS).register(addon);

        new ModulatedTurret(
                Groups.PRIMITIVE,
                TURRET_1_PASSIVE,
                RecipeType.NULL,
                new ItemStack[]{},
                TURRET_1_PASSIVE_SETTINGS).register(addon);

        new MultiblockClicker(
                Groups.PRIMITIVE,
                MULTIBLOCK_CLICKER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                MULTIBLOCK_CLICKER_1_SETTINGS).register(addon);
    }
}
