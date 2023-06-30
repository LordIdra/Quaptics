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

public class Advanced {
    public static final ConnectedBlock.Settings ENERGY_CONCENTRATOR_3_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.15F)
            .connectionRadius(0.4F)
            .emissionPower(1250)
            .build();
    public static final ConnectedBlock.Settings LENS_4_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.15F)
            .connectionRadius(0.30F)
            .powerLoss(0.02)
            .build();
    public static final ConnectedBlock.Settings COMBINER_4_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .powerLoss(0.05)
            .connections(2)
            .build();
    public static final ConnectedBlock.Settings COMBINER_4_3_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .powerLoss(0.05)
            .connections(3)
            .build();
    public static final ConnectedBlock.Settings COMBINER_4_4_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.05)
            .connections(4)
            .build();
    public static final ConnectedBlock.Settings SPLITTER_4_2_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .powerLoss(0.05)
            .connections(2)
            .build();
    public static final ConnectedBlock.Settings SPLITTER_4_3_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .powerLoss(0.05)
            .connections(3)
            .build();
    public static final ConnectedBlock.Settings SPLITTER_4_4_SETTINGS = ConnectedBlock.Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.05)
            .connections(4)
            .build();

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_3 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_3",
            Tier.ADVANCED.material,
            "&eEnergy Concentrator &bIII",
            Lore.create(ENERGY_CONCENTRATOR_3_SETTINGS,
                    "&7● Consumes energy",
                    "&7● Concentrates energy into a quaptic ray"));

    public static final SlimefunItemStack LENS_4 = new SlimefunItemStack(
            "QP_LENS_4",
            Material.GLASS,
            "&9Lens &bIV",
            Lore.create(LENS_4_SETTINGS,
                    "&7● Redirects a quaptic ray"));

    public static final SlimefunItemStack COMBINER_4_2 = new SlimefunItemStack(
            "QP_COMBINER_4_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIV &8(2 connections)",
            Lore.create(COMBINER_4_2_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public static final SlimefunItemStack COMBINER_4_3 = new SlimefunItemStack(
            "QP_COMBINER_4_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIV &8(3 connections)",
            Lore.create(COMBINER_4_3_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public static final SlimefunItemStack COMBINER_4_4 = new SlimefunItemStack(
            "QP_COMBINER_4_4",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIV &8(4 connections)",
            Lore.create(COMBINER_4_4_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public static final SlimefunItemStack SPLITTER_4_2 = new SlimefunItemStack(
            "QP_SPLITTER_4_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(2 connections)",
            Lore.create(SPLITTER_4_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public static final SlimefunItemStack SPLITTER_4_3 = new SlimefunItemStack(
            "QP_SPLITTER_4_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(3 connections)",
            Lore.create(SPLITTER_4_3_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public static final SlimefunItemStack SPLITTER_4_4 = new SlimefunItemStack(
            "QP_SPLITTER_4_4",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(4 connections)",
            Lore.create(SPLITTER_4_4_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new EnergyConcentrator(
                Groups.ADVANCED,
                ENERGY_CONCENTRATOR_3,
                RecipeType.NULL,
                new ItemStack[]{},
                ENERGY_CONCENTRATOR_3_SETTINGS,
                680,
                680).register(addon);

        new Lens(
                Groups.ADVANCED,
                LENS_4,
                RecipeType.NULL,
                new ItemStack[]{},
                LENS_4_SETTINGS).register(addon);

        new Combiner(
                Groups.ADVANCED,
                COMBINER_4_2,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_4_2_SETTINGS).register(addon);

        new Combiner(
                Groups.ADVANCED,
                COMBINER_4_3,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_4_3_SETTINGS).register(addon);

        new Combiner(
                Groups.ADVANCED,
                COMBINER_4_4,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_4_4_SETTINGS).register(addon);

        new Splitter(
                Groups.ADVANCED,
                SPLITTER_4_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_4_2_SETTINGS).register(addon);

        new Splitter(
                Groups.ADVANCED,
                SPLITTER_4_3,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_4_3_SETTINGS).register(addon);

        new Splitter(
                Groups.ADVANCED,
                SPLITTER_4_4,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_4_4_SETTINGS).register(addon);
    }
}
