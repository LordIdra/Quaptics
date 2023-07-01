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
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.QuapticTicker;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class QuapticChargeableItem extends SlimefunItem {
    @Getter
    protected final Settings settings;

    protected QuapticChargeableItem(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                                    final Settings settings) {
        super(itemGroup, item, recipeType, recipe);

        this.settings = settings;
    }

    public static Optional<QuapticChargeableItem> fromStack(@Nullable final ItemStack stack) {
        if (!(SlimefunItem.getByItem(stack) instanceof final QuapticChargeableItem chargeableItem)) {
            return Optional.empty();
        }

        return Optional.of(chargeableItem);
    }

    public static double getCharge(@NotNull final ItemStack stack) {
        return PersistentDataAPI.getDouble(stack.getItemMeta(), Keys.CHARGE, 0.0);
    }

    private static void setCharge(@NotNull final ItemStack stack, final double newCharge) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.setDouble(meta, Keys.CHARGE, newCharge);
        stack.setItemMeta(meta);
    }

    public static void chargeItem(@NotNull final ConnectionGroup connectionGroup, @NotNull final ItemDisplay display) {
        final Optional<ConnectionPointInput> input = connectionGroup.getInput("input");
        if (input.isEmpty() || !input.get().isLinkEnabled()) {
            return;
        }

        final ItemStack itemStack = display.getItemStack();
        final Optional<QuapticChargeableItem> item = fromStack(itemStack);
        if (item.isEmpty()) {
            return;
        }

        final Settings itemSettings = item.get().settings;
        final Optional<Link> link = input.get().getLink();
        if (link.isEmpty() || !meetsRequirements(itemSettings, link.get())) {
            return;
        }

        if (itemStack == null || itemStack.getItemMeta() == null) {
            return;
        }

        final double newCharge = itemSettings.stepCharge(getCharge(itemStack), link.get().getPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND);

        setCharge(itemStack, newCharge);
        display.setItemStack(itemStack);
    }

    private static boolean meetsRequirements(@NotNull final Settings itemSettings, @NotNull final Link link) {
        return itemSettings.checkFrequency(link.getFrequency());
    }

    private static int getFirstLineMatching(@NotNull final List<String> lore, final Predicate<? super String> matcher) {
        return IntStream.range(0, lore.size())
                .filter(i -> matcher.test(ChatColor.stripColor(lore.get(i))))
                .findFirst()
                .orElse(-1);
    }

    public static void updateLore(final ItemStack itemStack) {
        if (!(SlimefunItem.getByItem(itemStack) instanceof final QuapticChargeableItem chargeableItem)) {
            return;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        final double currentCharge = PersistentDataAPI.getDouble(itemMeta, Keys.CHARGE, 0.0);
        final Settings itemSettings = chargeableItem.settings;
        final List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
        final int chargeBar = getFirstLineMatching(lore, line -> line.contains("¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦"));
        final int chargeValues = getFirstLineMatching(lore, line -> Stream.of("⇨ ◆ ", " / ", "QEU").allMatch(line::contains));

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
