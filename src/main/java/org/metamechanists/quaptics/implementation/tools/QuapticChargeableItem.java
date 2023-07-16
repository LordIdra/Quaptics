package org.metamechanists.quaptics.implementation.tools;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.ChargeHolder;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class QuapticChargeableItem extends SlimefunItem implements ChargeHolder {
    @Getter
    protected final Settings settings;

    protected QuapticChargeableItem(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                                    final Settings settings) {
        super(itemGroup, item, recipeType, recipe);

        this.settings = settings;

        addItemHandler(onItemUse());
    }

    private @NotNull ItemUseHandler onItemUse() {
        return event -> {
            if (event.getSlimefunBlock().isPresent() && event.getSlimefunBlock().get() instanceof Charger) {
                return;
            }

            event.cancel();
            onUseItem(event);
        };
    }

    protected void onUseItem(final PlayerRightClickEvent event) {}

    private static Optional<QuapticChargeableItem> fromStack(@Nullable final ItemStack stack) {
        if (!(SlimefunItem.getByItem(stack) instanceof final QuapticChargeableItem chargeableItem)) {
            return Optional.empty();
        }

        return Optional.of(chargeableItem);
    }

    public static double getCharge(@NotNull final ItemStack stack) {
        return PersistentDataAPI.getDouble(stack.getItemMeta(), Keys.CHARGE, 0.0);
    }

    public static double getCapacity(@NotNull final ItemStack itemStack) {
        final Optional<QuapticChargeableItem> item = fromStack(itemStack);
        return item.map(quapticChargeableItem -> quapticChargeableItem.settings.getChargeCapacity()).orElse(0.0);

    }

    protected static void setCharge(@NotNull final ItemStack stack, final double newCharge) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.setDouble(meta, Keys.CHARGE, newCharge);
        stack.setItemMeta(meta);
    }

    public @NotNull ItemStack chargeItem(final Link inputLink, @NotNull final ItemStack itemStack, final double tickInterval) {
        if (itemStack.getItemMeta() == null) {
            return itemStack;
        }

        final Optional<QuapticChargeableItem> item = fromStack(itemStack);
        if (item.isEmpty()) {
            return itemStack;
        }

        final double newCharge = stepCharge(item.get().settings, getCharge(itemStack), inputLink.getPower() * (tickInterval / QuapticTicker.TICKS_PER_SECOND));
        setCharge(itemStack, newCharge);
        return itemStack;
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
            lore.set(chargeBar, ChatColors.color(Lore.chargeBar((int) currentCharge, (int) itemSettings.getChargeCapacity())));
        }

        if (chargeValues != -1) {
            lore.set(chargeValues, ChatColors.color(Lore.chargeValues((int) currentCharge, (int) itemSettings.getChargeCapacity())));
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}
