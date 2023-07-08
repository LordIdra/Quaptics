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
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

import java.util.Set;

@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Basic {
    public final Settings SOLAR_CONCENTRATOR_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.35F)
            .connectionRadius(0.35F)
            .rotationY((float)(Math.PI/4))
            .emissionPower(10)
            .build();
    public final Settings ENERGY_CONCENTRATOR_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.2F)
            .connectionRadius(0.3F)
            .emissionPower(15)
            .energyCapacity(30)
            .energyConsumption(30)
            .build();
    public final Settings LENS_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.21F)
            .connectionRadius(0.42F)
            .powerLoss(0.07)
            .build();
    public final Settings COMBINER_2_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.14)
            .connections(2)
            .build();
    public final Settings COMBINER_2_3_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.14)
            .connections(3)
            .build();
    public final Settings SPLITTER_2_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.14)
            .connections(2)
            .build();
    public final Settings SPLITTER_2_3_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.14)
            .connections(3)
            .build();
    public final Settings REPEATER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .minPower(15)
            .powerLoss(0.05)
            .minFrequency(0.0)
            .maxFrequency(0.3)
            .frequencyStep(0.1)
            .repeaterDelay(1)
            .build();

    public final Settings SCATTERER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .minPower(40)
            .powerLoss(0.05)
            .minFrequency(0.2)
            .maxFrequency(1.0)
            .frequencyMultiplier(2.0)
            .comparatorVisual("compare")
            .build();

    public final Settings TURRET_2_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.60F)
            .connectionRadius(0.65F)
            .minPower(40)
            .minFrequency(1.0)
            .range(7)
            .damage(2)
            .targets(Set.of(SpawnCategory.MONSTER))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.SMOOTH_STONE)
            .build();

    public final Settings TURRET_2_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.60F)
            .connectionRadius(0.65F)
            .minPower(40)
            .minFrequency(6)
            .range(7)
            .damage(2)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.SMOOTH_STONE)
            .build();

    public final SlimefunItemStack SOLAR_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_2",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bII",
            Lore.create(SOLAR_CONCENTRATOR_2_SETTINGS,
                    "&7● Only works during the day",
                    "&7● Concentrates sunlight into a quaptic ray"));

    public final SlimefunItemStack ENERGY_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_1",
            Tier.BASIC.concreteMaterial,
            "&eEnergy Concentrator &bI",
            Lore.create(ENERGY_CONCENTRATOR_1_SETTINGS,
                    "&7● Consumes energy",
                    "&7● Concentrates energy into a quaptic ray"));

    public final SlimefunItemStack LENS_2 = new SlimefunItemStack(
            "QP_LENS_2",
            Material.GLASS,
            "&9Lens &bII",
            Lore.create(LENS_2_SETTINGS,
                    "&7● &bRedirects &7a quaptic ray"));

    public final SlimefunItemStack COMBINER_2_2 = new SlimefunItemStack(
            "QP_COMBINER_2_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eII &8(2 connections)",
            Lore.create(COMBINER_2_2_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public final SlimefunItemStack COMBINER_2_3 = new SlimefunItemStack(
            "QP_COMBINER_2_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eII &8(3 connections)",
            Lore.create(COMBINER_2_3_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public final SlimefunItemStack SPLITTER_2_2 = new SlimefunItemStack(
            "QP_SPLITTER_2_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII &8(2 connections)",
            Lore.create(SPLITTER_2_2_SETTINGS,
                "&7● Splits one quaptic ray into multiple"));

    public final SlimefunItemStack SPLITTER_2_3 = new SlimefunItemStack(
            "QP_SPLITTER_2_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII &8(3 connections)",
            Lore.create(SPLITTER_2_3_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public final SlimefunItemStack REPEATER_1 = new SlimefunItemStack(
            "QP_REPEATER_1",
            Material.RED_STAINED_GLASS,
            "&cRepeater &4I",
            Lore.create(REPEATER_1_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));

    public final SlimefunItemStack SCATTERER_1 = new SlimefunItemStack(
            "QP_SCATTERER_1",
            Material.ORANGE_STAINED_GLASS,
            "&cScatterer &4I",
            Lore.create(SCATTERER_1_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));

    public final SlimefunItemStack TURRET_2_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_2_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eII &8(targets hostiles)",
            Lore.create(TURRET_2_HOSTILE_SETTINGS,
                    "&7● Direct beam",
                    "&7● Shoots at nearby entities"));

    public final SlimefunItemStack TURRET_2_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_2_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eII &8(targets passives)",
            Lore.create(TURRET_2_HOSTILE_SETTINGS,
                    "&7● Direct beam",
                    "&7● Shoots at nearby entities"));

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(
                Groups.BASIC,
                SOLAR_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SOLAR_CONCENTRATOR_2_SETTINGS).register(addon);

        new EnergyConcentrator(
                Groups.BASIC,
                ENERGY_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                ENERGY_CONCENTRATOR_1_SETTINGS).register(addon);

        new Lens(
                Groups.BASIC,
                LENS_2,
                RecipeType.NULL,
                new ItemStack[]{},
                LENS_2_SETTINGS).register(addon);

        new Combiner(
                Groups.BASIC,
                COMBINER_2_2,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_2_2_SETTINGS).register(addon);

        new Combiner(
                Groups.BASIC,
                COMBINER_2_3,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_2_3_SETTINGS).register(addon);

        new Splitter(
                Groups.BASIC,
                SPLITTER_2_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_2_2_SETTINGS).register(addon);

        new Splitter(
                Groups.BASIC,
                SPLITTER_2_3,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_2_3_SETTINGS).register(addon);

        new Repeater(
                Groups.BASIC,
                REPEATER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                REPEATER_1_SETTINGS).register(addon);

        new Scatterer(
                Groups.BASIC,
                SCATTERER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                SCATTERER_1_SETTINGS).register(addon);

        new DirectTurret(
                Groups.BASIC,
                TURRET_2_HOSTILE,
                RecipeType.NULL,
                new ItemStack[]{},
                TURRET_2_HOSTILE_SETTINGS).register(addon);

        new DirectTurret(
                Groups.BASIC,
                TURRET_2_PASSIVE,
                RecipeType.NULL,
                new ItemStack[]{},
                TURRET_2_PASSIVE_SETTINGS).register(addon);
    }
}
