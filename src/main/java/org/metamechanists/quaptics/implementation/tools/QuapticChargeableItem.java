package org.metamechanists.quaptics.implementation.tools;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.QuapticTicker;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public abstract class QuapticChargeableItem extends SlimefunItem {
    @Getter
    protected final Settings settings;

    protected QuapticChargeableItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Settings settings) {
        super(itemGroup, item, recipeType, recipe);

        this.settings = settings;
    }

    public static double getCharge(@NotNull ItemStack stack) {
        return PersistentDataAPI.getDouble(stack.getItemMeta(), Keys.CHARGE, 0.0);
    }

    private static void setCharge(@NotNull ItemStack stack, double newCharge) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.setDouble(meta, Keys.CHARGE, newCharge);
        stack.setItemMeta(meta);
    }

    public static void chargeItem(ConnectionGroup connectionGroup, @NotNull ItemDisplay display) {
        final ItemStack itemStack = display.getItemStack();
        if (!(SlimefunItem.getByItem(itemStack) instanceof QuapticChargeableItem chargeableItem)) {
            return;
        }

        final Settings itemSettings = chargeableItem.getSettings();
        final Link link = connectionGroup.getInput("input").getLink();
        if (!meetsRequirements(itemSettings, link)) {
            return;
        }

        if (itemStack.getItemMeta() == null) {
            return;
        }

        final double newCharge = itemSettings.stepCharge(getCharge(itemStack), link.getPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND);

        setCharge(itemStack, newCharge);
        display.setItemStack(itemStack);
    }

    public static boolean meetsRequirements(@NotNull Settings itemSettings, @NotNull Link link) {
        return itemSettings.checkFrequency(link.getFrequency());
    }

    private static int getFirstLineMatching(@NotNull List<String> lore, Predicate<String> matcher) {
        return IntStream.range(0, lore.size())
                .filter(i -> matcher.test(ChatColor.stripColor(lore.get(i))))
                .findFirst()
                .orElse(-1);
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
        final Settings itemSettings = chargeableItem.getSettings();
        final List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
        final int chargeBar = getFirstLineMatching(lore, line -> line.contains("¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦"));
        final int chargeValues = getFirstLineMatching(lore, line -> line.contains("⇨ ◆ ") && line.contains(" / ") && line.contains("QEU"));

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