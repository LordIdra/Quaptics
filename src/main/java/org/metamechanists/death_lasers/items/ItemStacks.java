package org.metamechanists.death_lasers.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.Material;

public class ItemStacks {
    public static final SlimefunItemStack EMITTER = new SlimefunItemStack(
            "EMITTER",
            Material.PURPLE_CONCRETE,
            "&bEmitter",
            "&7● Uses energy to &bemit &7a beam",
            LoreBuilder.powerPerSecond(100),
            Lore.emissionPower(5));

    public static final SlimefunItemStack LENS = new SlimefunItemStack(
            "LENS",
            Material.GLASS,
            "&9Lens",
            "&7● &bRedirects &7a laser beam",
            "&7● Power loss increases with input power",
            Lore.maxPower(20),
            Lore.powerLoss(30));

    public static final SlimefunItemStack DARK_PRISM = new SlimefunItemStack(
            "DARK_PRISM",
            Material.GRAY_STAINED_GLASS,
            "&8Dark Prism",
            "&7● &bConstitutes &7multiple laser beams into one",
            "&7● Power loss increases with input power",
            Lore.maxPower(40),
            Lore.powerLoss(40));

    public static final SlimefunItemStack WHITE_PRISM = new SlimefunItemStack(
            "WHITE_PRISM",
            Material.WHITE_STAINED_GLASS,
            "&fWhite Prism",
            "&7● &bSplits &7one laser beam into multiple",
            "&7● Power loss increases with input power",
            Lore.maxPower(40),
            Lore.powerLoss(40));

    public static final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "TARGETING_WAND",
            Material.BLAZE_ROD,
            "&6Targeting Wand",
            "&7● &eRight Click &7to select a source",
            "&7● &eRight Click &7again to create a link",
            "&7● &eShift Right Click &7to remove a link");
}
