package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.Colors;


@UtilityClass
public class Guide {
    private final SlimefunItemStack GETTING_STARTED = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED", Material.WHITE_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Getting Started",
            Lore.clickToOpen());
    private final SlimefunItemStack GETTING_STARTED_1 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_1", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 1",
            "&7Craft the following:",
            "&7- &eSolar Concentrator I",
            "&7- &eLens I",
            "&7- &eTargeting Wand");
    private final SlimefunItemStack GETTING_STARTED_2 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_2", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 2",
            "&7Place down the &eSolar Concentrator I &7and",
            "&7the &eLens I &7close to each other");
    private final SlimefunItemStack GETTING_STARTED_3 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_3", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 3",
            "&7Hold the targeting wand and right click",
            "&7the &coutput &7on the &eSolar Concentrator I");
    private final SlimefunItemStack GETTING_STARTED_4 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_4", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 4",
            "&7You've now selected an output. Next, right click",
            "&7the &ainput &7on the &eLens I&7. You might",
            "&7need to select the &coutput &7again if you've",
            "&7deselected it");
    private final SlimefunItemStack GETTING_STARTED_5 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_5", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 5",
            "&7You've now created your first Quaptic Link!");

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SlimefunItem(Groups.GUIDE, GETTING_STARTED, RecipeType.NULL, new ItemStack[]{
                GETTING_STARTED_1, GETTING_STARTED_2, GETTING_STARTED_3,
                GETTING_STARTED_4, GETTING_STARTED_5, null,
                null, null, null
        }).register(addon);
    }
}
