package org.metamechanists.death_lasers.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import org.bukkit.Material;

public class ItemStacks {
    public static final SlimefunItemStack EMITTER = new SlimefunItemStack(
            "EMITTER",
            Material.GLASS,
            "&4Emitter",
            LoreBuilder.powerPerSecond(100),
            Lore.emissionPower(10));
    public static final SlimefunItemStack LENS = new SlimefunItemStack(
            "LENS",
            Material.GLASS,
            "&4Lens",
            "&7Power loss increases with input power",
            Lore.maxPower(20),
            Lore.powerLoss(50));

    public static final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "TARGETING_WAND",
            Material.BLAZE_ROD,
            "&6Targeting Wand",
            "&eRight Click &7to select a source",
            "&eRight Click &7again to create a link",
            "&eShift Right Click &7to remove a link");
}
