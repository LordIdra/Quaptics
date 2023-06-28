package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.Material;

public class ItemStacks {
    public static final SlimefunItemStack SOLAR_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_1",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bI",
            Tiers.PRIMITIVE.coloredName,
            "&7● Only works during the day",
            "&7● Concentrates sunlight into a quaptic ray",
            Lore.emissionPower(1));

    public static final SlimefunItemStack SOLAR_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_2",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bII",
            Tiers.BASIC.coloredName,
            "&7● Only works during the day",
            "&7● Concentrates sunlight into a quaptic ray",
            Lore.emissionPower(10));

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_1",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bI",
            Tiers.BASIC.coloredName,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(30),
            Lore.emissionPower(15));

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_2",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bII",
            Tiers.INTERMEDIATE.coloredName,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(160),
            Lore.emissionPower(200));

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_3 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_3",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bIII",
            Tiers.ADVANCED.coloredName,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(680),
            Lore.emissionPower(2500));

    public static final SlimefunItemStack LENS_1 = new SlimefunItemStack(
            "QP_LENS_1",
            Material.GLASS,
            "&9Lens &bI",
            Tiers.PRIMITIVE.coloredName,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(Tiers.PRIMITIVE.maxPower),
            Lore.powerLoss(10));

    public static final SlimefunItemStack LENS_2 = new SlimefunItemStack(
            "QP_LENS_2",
            Material.GLASS,
            "&9Lens &bII",
            Tiers.BASIC.coloredName,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(Tiers.BASIC.maxPower),
            Lore.powerLoss(7));

    public static final SlimefunItemStack LENS_3 = new SlimefunItemStack(
            "QP_LENS_3",
            Material.GLASS,
            "&9Lens &bIII",
            Tiers.INTERMEDIATE.coloredName,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(Tiers.INTERMEDIATE.maxPower),
            Lore.powerLoss(4));

    public static final SlimefunItemStack LENS_4 = new SlimefunItemStack(
            "QP_LENS_4",
            Material.GLASS,
            "&9Lens &bIV",
            Tiers.ADVANCED.coloredName,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(Tiers.ADVANCED.maxPower),
            Lore.powerLoss(2));

    public static final SlimefunItemStack COMBINER_1 = new SlimefunItemStack(
            "QP_COMBINER_1",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eI",
            Tiers.PRIMITIVE.coloredName,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(Tiers.PRIMITIVE.maxPower),
            Lore.powerLoss(20),
            Lore.maxConnections(2));

    public static final SlimefunItemStack COMBINER_2 = new SlimefunItemStack(
            "QP_COMBINER_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eII",
            Tiers.BASIC.coloredName,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(Tiers.BASIC.maxPower),
            Lore.powerLoss(14),
            Lore.maxConnections(3));

    public static final SlimefunItemStack COMBINER_3 = new SlimefunItemStack(
            "QP_COMBINER_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIII",
            Tiers.INTERMEDIATE.coloredName,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(Tiers.INTERMEDIATE.maxPower),
            Lore.powerLoss(8),
            Lore.maxConnections(4));

    public static final SlimefunItemStack COMBINER_4 = new SlimefunItemStack(
            "QP_COMBINER_4",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIV",
            Tiers.ADVANCED.coloredName,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(Tiers.ADVANCED.maxPower),
            Lore.powerLoss(5),
            Lore.maxConnections(5));

    public static final SlimefunItemStack SPLITTER_1 = new SlimefunItemStack(
            "QP_SPLITTER_1",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eI",
            Tiers.PRIMITIVE.coloredName,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(Tiers.PRIMITIVE.maxPower),
            Lore.powerLoss(20),
            Lore.maxConnections(2));

    public static final SlimefunItemStack SPLITTER_2 = new SlimefunItemStack(
            "QP_SPLITTER_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII",
            Tiers.BASIC.coloredName,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(Tiers.BASIC.maxPower),
            Lore.powerLoss(14),
            Lore.maxConnections(3));

    public static final SlimefunItemStack SPLITTER_3 = new SlimefunItemStack(
            "QP_SPLITTER_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII",
            Tiers.INTERMEDIATE.coloredName,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(Tiers.INTERMEDIATE.maxPower),
            Lore.powerLoss(8),
            Lore.maxConnections(4));

    public static final SlimefunItemStack SPLITTER_4 = new SlimefunItemStack(
            "QP_SPLITTER_4",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV",
            Tiers.ADVANCED.coloredName,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(Tiers.ADVANCED.maxPower),
            Lore.powerLoss(5),
            Lore.maxConnections(5));

    public static final SlimefunItemStack TURRET = new SlimefunItemStack(
            "QP_TURRET",
            Material.SMOOTH_STONE_SLAB,
            "&4Turret",
            "&7● Shoots at nearby entities",
            Lore.minPower(5),
            Lore.damage(4),
            Lore.range(10));

    public static final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "QP_TARGETING_WAND",
            Material.BLAZE_ROD,
            "&6Targeting Wand",
            "&7● &eRight Click &7to select a source",
            "&7● &eRight Click &7again to create a link",
            "&7● &eShift Right Click &7to remove a link");
}
