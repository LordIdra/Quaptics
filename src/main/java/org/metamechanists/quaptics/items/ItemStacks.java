package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.Material;

public class ItemStacks {
    public static final SlimefunItemStack SOLAR_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_1",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bI",
            Lore.PRIMITIVE,
            "&7● Only works during the day",
            "&7● Concentrates sunlight into a quaptic ray",
            Lore.emissionPower(1));

    public static final SlimefunItemStack SOLAR_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_2",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bII",
            Lore.BASIC,
            "&7● Only works during the day",
            "&7● Concentrates sunlight into a quaptic ray",
            Lore.emissionPower(10));

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_1",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bI",
            Lore.BASIC,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(30),
            Lore.emissionPower(15));

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_2",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bII",
            Lore.MID,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(160),
            Lore.emissionPower(200));

    public static final SlimefunItemStack ENERGY_CONCENTRATOR_3 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_3",
            Material.PURPLE_CONCRETE,
            "&eEnergy Concentrator &bIII",
            Lore.ADVANCED,
            "&7● Consumes energy",
            "&7● Concentrates energy into a quaptic ray",
            LoreBuilder.powerPerSecond(680),
            Lore.emissionPower(2500));

    public static final SlimefunItemStack LENS_1 = new SlimefunItemStack(
            "QP_LENS_1",
            Material.GLASS,
            "&9Lens &bI",
            Lore.PRIMITIVE,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(10),
            Lore.powerLoss(10));

    public static final SlimefunItemStack LENS_2 = new SlimefunItemStack(
            "QP_LENS_2",
            Material.GLASS,
            "&9Lens &bII",
            Lore.BASIC,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(100),
            Lore.powerLoss(7));

    public static final SlimefunItemStack LENS_3 = new SlimefunItemStack(
            "QP_LENS_3",
            Material.GLASS,
            "&9Lens &bIII",
            Lore.MID,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(1000),
            Lore.powerLoss(4));

    public static final SlimefunItemStack LENS_4 = new SlimefunItemStack(
            "QP_LENS_4",
            Material.GLASS,
            "&9Lens &bIV",
            Lore.ADVANCED,
            "&7● &bRedirects &7a quaptic ray",
            Lore.maxPower(10000),
            Lore.powerLoss(2));

    public static final SlimefunItemStack COMBINER_1 = new SlimefunItemStack(
            "QP_COMBINER_1",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eI",
            Lore.PRIMITIVE,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(10),
            Lore.powerLoss(20));

    public static final SlimefunItemStack COMBINER_2 = new SlimefunItemStack(
            "QP_COMBINER_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eII",
            Lore.BASIC,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(100),
            Lore.powerLoss(14));

    public static final SlimefunItemStack COMBINER_3 = new SlimefunItemStack(
            "QP_COMBINER_3",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIII",
            Lore.MID,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(1000),
            Lore.powerLoss(8));

    public static final SlimefunItemStack COMBINER_4 = new SlimefunItemStack(
            "QP_COMBINER_4",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eIV",
            Lore.ADVANCED,
            "&7● &bCombines &7multiple quaptic rays into one",
            Lore.maxPower(10000),
            Lore.powerLoss(5));

    public static final SlimefunItemStack SPLITTER_1 = new SlimefunItemStack(
            "QP_SPLITTER_1",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eI",
            Lore.PRIMITIVE,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(10),
            Lore.powerLoss(20));

    public static final SlimefunItemStack SPLITTER_2 = new SlimefunItemStack(
            "QP_SPLITTER_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII",
            Lore.BASIC,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(100),
            Lore.powerLoss(14));

    public static final SlimefunItemStack SPLITTER_3 = new SlimefunItemStack(
            "QP_SPLITTER_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII",
            Lore.MID,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(1000),
            Lore.powerLoss(8));

    public static final SlimefunItemStack SPLITTER_4 = new SlimefunItemStack(
            "QP_SPLITTER_4",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV",
            Lore.ADVANCED,
            "&7● &bSplits &7one quaptic ray into multiple",
            Lore.maxPower(10000),
            Lore.powerLoss(5));

    public static final SlimefunItemStack TURRET = new SlimefunItemStack(
            "QP_TURRET",
            Material.SMOOTH_STONE_SLAB,
            "&4Turret",
            "&7● Shoots at nearby entities",
            Lore.minPower(30),
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
