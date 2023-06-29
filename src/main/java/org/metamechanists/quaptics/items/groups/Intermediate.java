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
import org.metamechanists.quaptics.utils.Colors;

public class Intermediate {
    private static final String TIER_NAME = Colors.INTERMEDIATE + "Intermediate";
    private static final float MAX_POWER = 1000;

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_2",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bII",
            TIER_NAME,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(160),
            Lore.emissionPower(200));

    public static final SlimefunItemStack LENS_3 = new SlimefunItemStack(
            "QP_LENS_3",
            Material.GLASS,
            "&9Lens &bIII",
            TIER_NAME,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(4));

    public static final SlimefunItemStack COMBINER_3_2 = new SlimefunItemStack(
            "QP_COMBINER_3_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIII &8(2 connections)",
            TIER_NAME,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(8),
            Lore.maxConnections(2));

    public static final SlimefunItemStack COMBINER_3_3 = new SlimefunItemStack(
            "QP_COMBINER_3_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIII &8(3 connections)",
            TIER_NAME,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(8),
            Lore.maxConnections(3));

    public static final SlimefunItemStack SPLITTER_3_2 = new SlimefunItemStack(
            "QP_SPLITTER_3_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII &8(2 connections)",
            TIER_NAME,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(8),
            Lore.maxConnections(2));

    public static final SlimefunItemStack SPLITTER_3_3 = new SlimefunItemStack(
            "QP_SPLITTER_3_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII &8(3 connections)",
            TIER_NAME,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(MAX_POWER),
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
                MAX_POWER).register(addon);

        new Lens(
                Groups.INTERMEDIATE,
                LENS_3,
                RecipeType.NULL,
                new ItemStack[]{},
                0.12F,
                MAX_POWER,
                0.04).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3_2,
                RecipeType.NULL,
                new ItemStack[]{},
                Material.YELLOW_CONCRETE,
                0.30F,
                2,
                MAX_POWER,
                0.08).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3_3,
                RecipeType.NULL,
                new ItemStack[]{},
                Material.YELLOW_CONCRETE,
                0.35F,
                3,
                MAX_POWER,
                0.08).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3_2,
                RecipeType.NULL,
                new ItemStack[]{},
                Material.YELLOW_CONCRETE,
                0.30F,
                2,
                MAX_POWER,
                0.08).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3_3,
                RecipeType.NULL,
                new ItemStack[]{},
                Material.YELLOW_CONCRETE,
                0.35F,
                3,
                MAX_POWER,
                0.08).register(addon);
    }
}
