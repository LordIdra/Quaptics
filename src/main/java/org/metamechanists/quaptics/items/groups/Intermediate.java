package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tiers;

public class Intermediate {
    public static final SlimefunItemStack ENERGY_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_2",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bII",
            Tiers.INTERMEDIATE.coloredName,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(160),
            Lore.emissionPower(200));

    public static final SlimefunItemStack LENS_3 = new SlimefunItemStack(
            "QP_LENS_3",
            Material.GLASS,
            "&9Lens &bIII",
            Tiers.INTERMEDIATE.coloredName,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(Tiers.INTERMEDIATE.maxPower),
            Lore.powerLoss(4));

    public static final SlimefunItemStack COMBINER_3_2 = new SlimefunItemStack(
            "QP_COMBINER_3_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIII &8(2 connections)",
            Tiers.INTERMEDIATE.coloredName,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(Tiers.INTERMEDIATE.maxPower),
            Lore.powerLoss(8),
            Lore.maxConnections(2));

    public static final SlimefunItemStack COMBINER_3_3 = new SlimefunItemStack(
            "QP_COMBINER_3_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIII &8(3 connections)",
            Tiers.INTERMEDIATE.coloredName,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(Tiers.INTERMEDIATE.maxPower),
            Lore.powerLoss(8),
            Lore.maxConnections(3));

    public static final SlimefunItemStack SPLITTER_3_2 = new SlimefunItemStack(
            "QP_SPLITTER_3_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII &8(2 connections)",
            Tiers.INTERMEDIATE.coloredName,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(Tiers.INTERMEDIATE.maxPower),
            Lore.powerLoss(8),
            Lore.maxConnections(2));

    public static final SlimefunItemStack SPLITTER_3_3 = new SlimefunItemStack(
            "QP_SPLITTER_3_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII &8(3 connections)",
            Tiers.INTERMEDIATE.coloredName,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(Tiers.INTERMEDIATE.maxPower),
            Lore.powerLoss(8),
            Lore.maxConnections(3));

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new EnergyConcentrator(
                Groups.INTERMEDIATE,
                ENERGY_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                160,
                160,
                200,
                Tiers.INTERMEDIATE.maxPower).register(addon);

        new Lens(
                Groups.INTERMEDIATE,
                LENS_3,
                RecipeType.NULL,
                new ItemStack[]{},
                0.12F,
                Tiers.INTERMEDIATE.maxPower,
                0.04).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3_2,
                RecipeType.NULL,
                new ItemStack[]{},
                0.30F,
                2,
                Tiers.INTERMEDIATE.maxPower,
                0.08).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3_3,
                RecipeType.NULL,
                new ItemStack[]{},
                0.35F,
                3,
                Tiers.INTERMEDIATE.maxPower,
                0.08).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3_2,
                RecipeType.NULL,
                new ItemStack[]{},
                0.30F,
                2,
                Tiers.INTERMEDIATE.maxPower,
                0.08).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3_3,
                RecipeType.NULL,
                new ItemStack[]{},
                0.35F,
                3,
                Tiers.INTERMEDIATE.maxPower,
                0.08).register(addon);
    }
}
