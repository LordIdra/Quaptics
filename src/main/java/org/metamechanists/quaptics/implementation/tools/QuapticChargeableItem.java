package org.metamechanists.quaptics.implementation.tools;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.metamechanists.quaptics.QuapticTicker;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.Keys;

import java.util.ArrayList;
import java.util.List;

public abstract class QuapticChargeableItem extends SlimefunItem {
    @Getter
    protected final ConnectedBlock.Settings settings;

    protected QuapticChargeableItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ConnectedBlock.Settings settings) {
        super(itemGroup, item, recipeType, recipe);

        this.settings = settings;
    }

    public static void chargeItem(ConnectionGroup connectionGroup, ItemDisplay display) {
        final ItemStack itemStack = display.getItemStack();
        if (!(SlimefunItem.getByItem(itemStack) instanceof QuapticChargeableItem chargeableItem)) {
            return;
        }

        final ConnectedBlock.Settings itemSettings = chargeableItem.getSettings();
        final ConnectedBlock.Settings chargerSettings = connectionGroup.getBlock().getSettings();
        final Link link = connectionGroup.getInput("input").getLink();
        if (!meetsRequirements(itemSettings, chargerSettings, link)) {
            return;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        final double currentCharge = PersistentDataAPI.getDouble(itemMeta, Keys.CHARGE, 0.0);
        final double newCharge = itemSettings.stepCharge(currentCharge, link.getPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND);
        PersistentDataAPI.setDouble(itemMeta, Keys.CHARGE, newCharge);
        itemStack.setItemMeta(itemMeta);
        display.setItemStack(itemStack);
    }

    public static boolean meetsRequirements(ConnectedBlock.Settings itemSettings, ConnectedBlock.Settings chargerSettings, Link link) {
        return itemSettings.checkFrequency(link.getFrequency());
    }

    public static void updateLore(ItemStack itemStack) {
        if (!(SlimefunItem.getByItem(itemStack) instanceof QuapticChargeableItem chargeableItem)) {
            return;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        final double currentCharge = PersistentDataAPI.getDouble(itemMeta, Keys.CHARGE, 0.0);
        final ConnectedBlock.Settings itemSettings = chargeableItem.getSettings();
        final List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
        int chargeBar = -1;
        int chargeValues = -1;
        int i = 0;

        for (String coloredLine : lore) {
            final String line = ChatColor.stripColor(coloredLine);
            if (line.contains("¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦") && chargeBar == -1) {
                chargeBar = i;
            } else if (line.contains("⇨ ◆ ") && line.contains(" / ") && line.contains("QEU") && chargeValues == -1) {
                chargeValues = i;
            }
            i++;
        }

        if (chargeBar != -1) {
            lore.set(chargeBar, ChatColors.color(Lore.chargeBar((int) currentCharge, (int) itemSettings.getCapacity())));
        }

        if (chargeValues != -1) {
            lore.set(chargeValues, ChatColors.color(Lore.chargeValues((int) currentCharge, (int) itemSettings.getCapacity())));
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}
