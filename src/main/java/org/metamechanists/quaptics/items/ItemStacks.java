package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.Material;

public class ItemStacks {
    public static final SlimefunItemStack EMITTER = new SlimefunItemStack(
            "EMITTER",
            Material.PURPLE_CONCRETE,
            "&bEmitter",
            "&7● Uses energy to &bemit &7a quaptic ray",
            LoreBuilder.powerPerSecond(100),
            Lore.emissionPower(20));

    public static final SlimefunItemStack LENS = new SlimefunItemStack(
            "LENS",
            Material.GLASS,
            "&9Lens",
            "&7● &bRedirects &7a quaptic ray",
            "&7● Power loss increases with input power",
            Lore.maxPower(20),
            Lore.powerLoss(10));

    public static final SlimefunItemStack COMBINER = new SlimefunItemStack(
            "COMBINER",
            Material.GRAY_STAINED_GLASS,
            "&8Combiner",
            "&7● &bCombines &7multiple quaptic rays into one",
            "&7● Power loss increases with input power",
            Lore.maxPower(40),
            Lore.powerLoss(20));

    public static final SlimefunItemStack SPLITTER = new SlimefunItemStack(
            "SPLITTER",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&fSplitter",
            "&7● &bSplits &7one quaptic ray into multiple",
            "&7● Power loss increases with input power",
            Lore.maxPower(40),
            Lore.powerLoss(20));

    public static final SlimefunItemStack TURRET = new SlimefunItemStack(
            "TURRET",
            Material.SMOOTH_STONE_SLAB,
            "&4Turret",
            "&7● Shoots at nearby entities",
            Lore.powerConsumption(30),
            Lore.damage(4),
            Lore.range(10));

    public static final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "TARGETING_WAND",
            Material.BLAZE_ROD,
            "&6Targeting Wand",
            "&7● &eRight Click &7to select a source",
            "&7● &eRight Click &7again to create a link",
            "&7● &eShift Right Click &7to remove a link");
}
