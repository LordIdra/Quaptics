package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

@SuppressWarnings({"MagicNumber", "ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Advanced {
    public final Settings ENERGY_CONCENTRATOR_3_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.15F)
            .connectionRadius(0.4F)
            .emissionPower(1250)
            .build();
    public final Settings LENS_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.15F)
            .connectionRadius(0.30F)
            .powerLoss(0.02)
            .build();
    public final Settings COMBINER_4_2_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .powerLoss(0.05)
            .connections(2)
            .build();
    public final Settings COMBINER_4_3_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .powerLoss(0.05)
            .connections(3)
            .build();
    public final Settings COMBINER_4_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.05)
            .connections(4)
            .build();
    public final Settings SPLITTER_4_2_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .powerLoss(0.05)
            .connections(2)
            .build();
    public final Settings SPLITTER_4_3_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .powerLoss(0.05)
            .connections(3)
            .build();
    public final Settings SPLITTER_4_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.05)
            .connections(4)
            .build();

    public final SlimefunItemStack ENERGY_CONCENTRATOR_3 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_3",
            Tier.ADVANCED.concreteMaterial,
            "&eEnergy Concentrator &bIII",
            Lore.create(ENERGY_CONCENTRATOR_3_SETTINGS,
                    "&7● Consumes energy",
                    "&7● Concentrates energy into a quaptic ray"));

    public final SlimefunItemStack LENS_4 = new SlimefunItemStack(
            "QP_LENS_4",
            Material.GLASS,
            "&9Lens &bIV",
            Lore.create(LENS_4_SETTINGS,
                    "&7● Redirects a quaptic ray"));

    public final SlimefunItemStack COMBINER_4_2 = new SlimefunItemStack(
            "QP_COMBINER_4_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIV &8(2 connections)",
            Lore.create(COMBINER_4_2_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public final SlimefunItemStack COMBINER_4_3 = new SlimefunItemStack(
            "QP_COMBINER_4_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIV &8(3 connections)",
            Lore.create(COMBINER_4_3_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public final SlimefunItemStack COMBINER_4_4 = new SlimefunItemStack(
            "QP_COMBINER_4_4",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIV &8(4 connections)",
            Lore.create(COMBINER_4_4_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public final SlimefunItemStack SPLITTER_4_2 = new SlimefunItemStack(
            "QP_SPLITTER_4_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(2 connections)",
            Lore.create(SPLITTER_4_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public final SlimefunItemStack SPLITTER_4_3 = new SlimefunItemStack(
            "QP_SPLITTER_4_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(3 connections)",
            Lore.create(SPLITTER_4_3_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public final SlimefunItemStack SPLITTER_4_4 = new SlimefunItemStack(
            "QP_SPLITTER_4_4",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(4 connections)",
            Lore.create(SPLITTER_4_4_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    private Advanced() {}

    public void initialize() {
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
