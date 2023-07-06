package org.metamechanists.quaptics.implementation.blocks.attachments;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metalib.utils.ItemUtils;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.base.DisplayGroupTickerBlock;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;

import java.util.Optional;

public interface ItemHolderBlock {
    static Optional<ItemDisplay> getItemDisplay(final Location location) {
        final Optional<Display> display = DisplayGroupTickerBlock.getDisplay(location, "item");
        if (display.isEmpty()) {
            return Optional.empty();
        }

        if (!(display.get() instanceof final ItemDisplay itemDisplay)) {
            return Optional.empty();
        }

        return Optional.of(itemDisplay);
    }

    static Optional<ItemStack> getStack(final @NotNull ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return Optional.empty();
        }

        return getStack(location.get());
    }

    static Optional<ItemStack> getStack(final @NotNull Location location) {
        final Optional<ItemDisplay> itemDisplay = getItemDisplay(location);
        if (itemDisplay.isEmpty()) {
            return Optional.empty();
        }

        final ItemStack stack = itemDisplay.get().getItemStack();
        return stack == null || stack.getItemMeta() == null ? Optional.empty() : Optional.of(stack);
    }

    static void insertItem(final Location location, @NotNull final ItemStack itemStack) {
        final Optional<ItemDisplay> itemDisplay = getItemDisplay(location);
        if (itemDisplay.isEmpty()) {
            return;
        }

        itemDisplay.get().setItemStack(itemStack);
    }

    static void insertItem(final Location location, @NotNull final Player player) {
        final ItemStack itemStack = player.getInventory().getItemInMainHand().clone();
        if (itemStack.getType().isEmpty()) {
            return;
        }

        if (!(SlimefunItem.getByItem(itemStack) instanceof QuapticChargeableItem)) {
            return;
        }

        insertItem(location, itemStack);
        player.getInventory().setItemInMainHand(null);
    }

    static void removeItem(@NotNull final Location location, final Player player) {
        final Optional<ItemDisplay> itemDisplay = getItemDisplay(location);
        if (itemDisplay.isEmpty()) {
            return;
        }

        final ItemStack itemStack = itemDisplay.get().getItemStack();
        if (itemStack == null) {
            return;
        }

        itemDisplay.get().setItemStack(null);
        QuapticChargeableItem.updateLore(itemStack);
        ItemUtils.addOrDropItemMainHand(player, itemStack);
    }
}
