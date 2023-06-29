package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.Colors;

public class Basic {
    private static final String TIER_NAME = Colors.BASIC + "Basic";
    private static final float MAX_POWER = 100;

    public static final SlimefunItemStack SOLAR_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_2",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bII",
            TIER_NAME,
            "&7● Only works during the day",
            "&7● Concentrates sunlight into a quaptic ray",
            Lore.emissionPower(10));

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_1",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bI",
            TIER_NAME,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(30),
            Lore.emissionPower(15));

    public static final SlimefunItemStack LENS_2 = new SlimefunItemStack(
            "QP_LENS_2",
            Material.GLASS,
            "&9Lens &bII",
            TIER_NAME,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(7));

    public static final SlimefunItemStack COMBINER_2_2 = new SlimefunItemStack(
            "QP_COMBINER_2_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eII &8(2 connections)",
            TIER_NAME,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(14),
            Lore.maxConnections(2));

    public static final SlimefunItemStack COMBINER_2_3 = new SlimefunItemStack(
            "QP_COMBINER_2_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eII &8(3 connections)",
            TIER_NAME,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(14),
            Lore.maxConnections(3));

    public static final SlimefunItemStack SPLITTER_2_2 = new SlimefunItemStack(
            "QP_SPLITTER_2_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII &8(2 connections)",
            TIER_NAME,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(14),
            Lore.maxConnections(2));

    public static final SlimefunItemStack SPLITTER_2_3 = new SlimefunItemStack(
            "QP_SPLITTER_2_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII &8(3 connections)",
            TIER_NAME,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(MAX_POWER),
            Lore.powerLoss(14),
            Lore.maxConnections(3));

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(
                Groups.BASIC,
                SOLAR_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                0.35F,
                (float)(Math.PI/4),
                10,
                MAX_POWER).register(addon);

        new EnergyConcentrator(
                Groups.BASIC,
                ENERGY_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                30,
                30,
                15,
                MAX_POWER).register(addon);

        new Lens(
                Groups.BASIC,
                LENS_2,
                RecipeType.NULL,
                new ItemStack[]{},
                0.21F,
                MAX_POWER,
                0.07).register(addon);

        new Combiner(
                Groups.BASIC,
                COMBINER_2_2,
                RecipeType.NULL,
                new ItemStack[]{},
                Material.GRAY_CONCRETE,
                0.35F,
                2,
                MAX_POWER,
                0.12).register(addon);

        new Combiner(
                Groups.BASIC,
                COMBINER_2_3,
                RecipeType.NULL,
                new ItemStack[]{},
                Material.GRAY_CONCRETE,
                0.40F,
                3,
                MAX_POWER,
                0.14).register(addon);

        new Splitter(
                Groups.BASIC,
                SPLITTER_2_2,
                RecipeType.NULL,
                new ItemStack[]{},
                Material.GRAY_CONCRETE,
                0.35F,
                2,
                MAX_POWER,
                0.12).register(addon);

        new Splitter(
                Groups.BASIC,
                SPLITTER_2_3,
                RecipeType.NULL,
                new ItemStack[]{},
                Material.GRAY_CONCRETE,
                0.40F,
                2,
                MAX_POWER,
                0.14).register(addon);
    }
}
