package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

public class Basic {
    public static final ConnectedBlock.Settings SOLAR_CONCENTRATOR_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.35F)
            .connectionRadius(0.35F)
            .emissionPower(10)
            .build();
    public static final ConnectedBlock.Settings ENERGY_CONCENTRATOR_1_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.2F)
            .connectionRadius(0.3F)
            .emissionPower(15)
            .build();
    public static final ConnectedBlock.Settings LENS_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.21F)
            .connectionRadius(0.42F)
            .powerLoss(0.07)
            .build();
    public static final ConnectedBlock.Settings COMBINER_2_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.14)
            .connections(2)
            .build();
    public static final ConnectedBlock.Settings COMBINER_2_3_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.14)
            .connections(3)
            .build();
    public static final ConnectedBlock.Settings SPLITTER_2_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.14)
            .connections(2)
            .build();
    public static final ConnectedBlock.Settings SPLITTER_2_3_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.14)
            .connections(3)
            .build();
    public static final ConnectedBlock.Settings REPEATER_1_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .minPower(15)
            .powerLoss(0.05)
            .minFrequency(0.0)
            .maxFrequency(0.2)
            .frequencyStep(0.1)
            .build();

    public static final ConnectedBlock.Settings SCATTERER_1_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .minPower(40)
            .powerLoss(0.05)
            .minFrequency(0.2)
            .maxFrequency(0.7)
            .frequencyMultiplier(2.0)
            .build();

    public static final ConnectedBlock.Settings REPEATER_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .minPower(65)
            .powerLoss(0.05)
            .minFrequency(0.7)
            .maxFrequency(1.0)
            .frequencyStep(0.1)
            .build();

    public static final SlimefunItemStack SOLAR_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_2",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bII",
            Lore.create(SOLAR_CONCENTRATOR_2_SETTINGS,
                    "&7● Only works during the day",
                    "&7● Concentrates sunlight into a quaptic ray"));

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_1",
            Tier.BASIC.concreteMaterial,
            "&eEnergy Concentrator &bI",
            Lore.create(ENERGY_CONCENTRATOR_1_SETTINGS,
                    "&7● Consumes energy",
                    "&7● Concentrates energy into a quaptic ray"));

    public static final SlimefunItemStack LENS_2 = new SlimefunItemStack(
            "QP_LENS_2",
            Material.GLASS,
            "&9Lens &bII",
            Lore.create(LENS_2_SETTINGS,
                    "&7● &bRedirects &7a quaptic ray"));

    public static final SlimefunItemStack COMBINER_2_2 = new SlimefunItemStack(
            "QP_COMBINER_2_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eII &8(2 connections)",
            Lore.create(COMBINER_2_2_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public static final SlimefunItemStack COMBINER_2_3 = new SlimefunItemStack(
            "QP_COMBINER_2_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eII &8(3 connections)",
            Lore.create(COMBINER_2_3_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public static final SlimefunItemStack SPLITTER_2_2 = new SlimefunItemStack(
            "QP_SPLITTER_2_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII &8(2 connections)",
            Lore.create(SPLITTER_2_2_SETTINGS,
                "&7● Splits one quaptic ray into multiple"));

    public static final SlimefunItemStack SPLITTER_2_3 = new SlimefunItemStack(
            "QP_SPLITTER_2_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII &8(3 connections)",
            Lore.create(SPLITTER_2_3_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public static final SlimefunItemStack REPEATER_1 = new SlimefunItemStack(
            "QP_REPEATER_1",
            Material.RED_STAINED_GLASS,
            "&cRepeater &4I",
            Lore.create(REPEATER_1_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));

    public static final SlimefunItemStack SCATTERER_1 = new SlimefunItemStack(
            "QP_SCATTERER_1",
            Material.ORANGE_STAINED_GLASS,
            "&cScatterer &4I",
            Lore.create(SCATTERER_1_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));

    public static final SlimefunItemStack REPEATER_2 = new SlimefunItemStack(
            "QP_REPEATER_2",
            Material.RED_STAINED_GLASS,
            "&cRepeater &4II",
            Lore.create(REPEATER_2_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(
                Groups.BASIC,
                SOLAR_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SOLAR_CONCENTRATOR_2_SETTINGS,
                (float)(Math.PI/4)).register(addon);

        new EnergyConcentrator(
                Groups.BASIC,
                ENERGY_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                ENERGY_CONCENTRATOR_1_SETTINGS,
                30,
                30).register(addon);

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
                REPEATER_1_SETTINGS,
                1).register(addon);

        new Scatterer(
                Groups.BASIC,
                SCATTERER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                SCATTERER_1_SETTINGS,
                "compare").register(addon);

        new Repeater(
                Groups.BASIC,
                REPEATER_2,
                RecipeType.NULL,
                new ItemStack[]{},
                REPEATER_2_SETTINGS,
                2).register(addon);
    }
}
