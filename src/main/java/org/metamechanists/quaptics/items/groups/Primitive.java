package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

import java.util.Set;

public class Primitive {
    public static final ConnectedBlock.Settings SOLAR_CONCENTRATOR_1_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.45F)
            .connectionRadius(0.45F)
            .emissionPower(1)
            .build();

    public static final ConnectedBlock.Settings LENS_1_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.24F)
            .connectionRadius(0.48F)
            .powerLoss(0.1)
            .build();

    public static final ConnectedBlock.Settings COMBINER_1_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.2)
            .connections(2)
            .build();

    public static final ConnectedBlock.Settings SPLITTER_1_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.2)
            .connections(2)
            .build();

    public static final ConnectedBlock.Settings CHARGER_1_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .build();

    public static final ConnectedBlock.Settings CAPACITOR_1_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .capacity(200)
            .emissionPower(3)
            .build();

    public static final ConnectedBlock.Settings TURRET_1_HOSTILE_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.55F)
            .connectionRadius(0.55F)
            .minPower(5)
            .range(5)
            .damage(1)
            .projectileSpeed(0.7F)
            .targets(Set.of(SpawnCategory.MONSTER))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.POLISHED_ANDESITE)
            .build();

    public static final ConnectedBlock.Settings TURRET_1_PASSIVE_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.55F)
            .connectionRadius(0.55F)
            .minPower(5)
            .range(5)
            .damage(1)
            .projectileSpeed(0.5F)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.POLISHED_ANDESITE)
            .build();

    public static final SlimefunItemStack SOLAR_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_1",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bI",
            Lore.create(SOLAR_CONCENTRATOR_1_SETTINGS,
                    "&7● Only works during the day",
                    "&7● Concentrates sunlight into a quaptic ray"));

    public static final SlimefunItemStack LENS_1 = new SlimefunItemStack(
            "QP_LENS_1",
            Material.GLASS,
            "&9Lens &bI",
            Lore.create(LENS_1_SETTINGS,
                    "&7● Redirects a quaptic ray"));

    public static final SlimefunItemStack COMBINER_1_2 = new SlimefunItemStack(
            "QP_COMBINER_1_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eI &8(2 connections)",
            Lore.create(COMBINER_1_2_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public static final SlimefunItemStack SPLITTER_1_2 = new SlimefunItemStack(
            "QP_SPLITTER_1_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eI &8(2 connections)",
            Lore.create(SPLITTER_1_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public static final SlimefunItemStack CHARGER_1 = new SlimefunItemStack(
            "QP_CHARGER",
            Material.LIGHT_BLUE_STAINED_GLASS,
            "&bCharger &3I",
            Lore.create(CHARGER_1_SETTINGS,
                    "&7● Charges item with Quaptic Energy Units",
                    "&7● &eRight Click &7an item to place",
                    "&7● &eRight Click &7again to retrieve"));

    public static final SlimefunItemStack CAPACITOR_1 = new SlimefunItemStack(
            "QP_CAPACITOR_1",
            Material.LIGHT_BLUE_CONCRETE,
            "&3Capacitor &bI",
            Lore.create(CAPACITOR_1_SETTINGS,
                    "&7● Stores charge",
                    "&7● Outputs at a constant power"));

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

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(
                Groups.PRIMITIVE,
                SOLAR_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                SOLAR_CONCENTRATOR_1_SETTINGS,
                0.0F).register(addon);

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
    }
}
