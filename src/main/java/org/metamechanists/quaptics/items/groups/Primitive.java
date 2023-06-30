package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.Turret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

public class Primitive {
    public static final SlimefunItemStack SOLAR_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_1",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bI",
            Tier.PRIMITIVE.name,
            "&7● Only works during the day",
            "&7● Concentrates sunlight into a quaptic ray",
            Lore.emissionPower(1));

    public static final SlimefunItemStack LENS_1 = new SlimefunItemStack(
            "QP_LENS_1",
            Material.GLASS,
            "&9Lens &bI",
            Tier.PRIMITIVE.name,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(Tier.PRIMITIVE.maxPower),
            Lore.powerLoss(10));

    public static final SlimefunItemStack COMBINER_1_2 = new SlimefunItemStack(
            "QP_COMBINER_1_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eI &8(2 connections)",
            Tier.PRIMITIVE.name,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(Tier.PRIMITIVE.maxPower),
            Lore.powerLoss(20),
            Lore.maxConnections(2));

    public static final SlimefunItemStack SPLITTER_1_2 = new SlimefunItemStack(
            "QP_SPLITTER_1_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eI &8(2 connections)",
            Tier.PRIMITIVE.name,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(Tier.PRIMITIVE.maxPower),
            Lore.powerLoss(20),
            Lore.maxConnections(2));

    public static final SlimefunItemStack TURRET = new SlimefunItemStack(
            "QP_TURRET",
            Material.SMOOTH_STONE_SLAB,
            "&4Turret",
            Tier.PRIMITIVE.name,
            "&7● Shoots at nearby entities",
            Lore.maxPower(Tier.PRIMITIVE.maxPower),
            Lore.minPower(5),
            Lore.damage(4),
            Lore.range(10));

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(
                Groups.PRIMITIVE,
                SOLAR_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                ConnectedBlock.Settings.builder()
                        .tier(Tier.PRIMITIVE)
                        .displayRadius(0.45F)
                        .connectionRadius(0.45F)
                        .powerEmission(1)
                        .build(),
                0.0F).register(addon);

        new Lens(
                Groups.PRIMITIVE,
                LENS_1,
                RecipeType.NULL,
                new ItemStack[]{},
                ConnectedBlock.Settings.builder()
                        .tier(Tier.PRIMITIVE)
                        .displayRadius(0.24F)
                        .connectionRadius(0.48F)
                        .powerLoss(0.1)
                        .build()).register(addon);

        new Combiner(
                Groups.PRIMITIVE,
                COMBINER_1_2,
                RecipeType.NULL,
                new ItemStack[]{},
                ConnectedBlock.Settings.builder()
                        .tier(Tier.PRIMITIVE)
                        .displayRadius(0.4F)
                        .connectionRadius(0.8F)
                        .connections(2)
                        .powerLoss(0.2)
                        .build()).register(addon);

        new Splitter(
                Groups.PRIMITIVE,
                SPLITTER_1_2,
                RecipeType.NULL,
                new ItemStack[]{},
                ConnectedBlock.Settings.builder()
                        .tier(Tier.PRIMITIVE)
                        .displayRadius(0.4F)
                        .connectionRadius(0.8F)
                        .connections(2)
                        .powerLoss(0.2)
                        .build()).register(addon);

        new Turret(
                Groups.PRIMITIVE,
                TURRET,
                RecipeType.NULL,
                new ItemStack[]{},
                ConnectedBlock.Settings.builder()
                        .tier(Tier.PRIMITIVE)
                        .minPower(5)
                        .range(10)
                        .damage(2)
                        .build()).register(addon);
    }
}
