package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

public class Intermediate {
    public static final ConnectedBlock.Settings ENERGY_CONCENTRATOR_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.175F)
            .connectionRadius(0.35F)
            .emissionPower(100)
            .build();
    public static final ConnectedBlock.Settings LENS_3_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.18F)
            .connectionRadius(0.36F)
            .powerLoss(0.04)
            .build();
    public static final ConnectedBlock.Settings COMBINER_3_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .powerLoss(0.08)
            .connections(2)
            .build();
    public static final ConnectedBlock.Settings COMBINER_3_3_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.08)
            .connections(3)
            .build();
    public static final ConnectedBlock.Settings SPLITTER_3_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .powerLoss(0.08)
            .connections(2)
            .build();
    public static final ConnectedBlock.Settings SPLITTER_3_3_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.08)
            .connections(3)
            .build();
    public static final SlimefunItemStack ENERGY_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_2",
            Tier.INTERMEDIATE.material,
            "&eEnergy Concentrator &bII",
            Lore.create(ENERGY_CONCENTRATOR_2_SETTINGS,
                    "&7● Consumes energy",
                    "&7● Concentrates energy into a quaptic ray"));

    public static final SlimefunItemStack LENS_3 = new SlimefunItemStack(
            "QP_LENS_3",
            Material.GLASS,
            "&9Lens &bIII",
            Lore.create(LENS_3_SETTINGS,
                    "&7● &bRedirects &7a quaptic ray"));

    public static final SlimefunItemStack COMBINER_3_2 = new SlimefunItemStack(
            "QP_COMBINER_3_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIII &8(2 connections)",
            Lore.create(COMBINER_3_2_SETTINGS,
                    "&7● &bCombines &7multiple quaptic rays into one"));

    public static final SlimefunItemStack COMBINER_3_3 = new SlimefunItemStack(
            "QP_COMBINER_3_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIII &8(3 connections)",
            Lore.create(COMBINER_3_3_SETTINGS,
                    "&7● &bCombines &7multiple quaptic rays into one"));

    public static final SlimefunItemStack SPLITTER_3_2 = new SlimefunItemStack(
            "QP_SPLITTER_3_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII &8(2 connections)",
            Lore.create(SPLITTER_3_2_SETTINGS,
                    "&7● &bSplits &7one quaptic ray into multiple"));

    public static final SlimefunItemStack SPLITTER_3_3 = new SlimefunItemStack(
            "QP_SPLITTER_3_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII &8(3 connections)",
            Lore.create(SPLITTER_3_3_SETTINGS,
                    "&7● &bSplits &7one quaptic ray into multiple"));

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new EnergyConcentrator(
                Groups.INTERMEDIATE,
                ENERGY_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                ENERGY_CONCENTRATOR_2_SETTINGS,
                160,
                160).register(addon);

        new Lens(
                Groups.INTERMEDIATE,
                LENS_3,
                RecipeType.NULL,
                new ItemStack[]{},
                LENS_3_SETTINGS).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3_2,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_3_2_SETTINGS).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3_3,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_3_3_SETTINGS).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_3_2_SETTINGS).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3_3,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_3_3_SETTINGS).register(addon);
    }
}
