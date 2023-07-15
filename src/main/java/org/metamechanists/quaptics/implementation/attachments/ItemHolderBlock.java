package org.metamechanists.quaptics.implementation.attachments;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metalib.utils.ItemUtils;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.base.QuapticBlock;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;

import java.util.Optional;

public interface ItemHolderBlock {
    static Optional<ItemStack> getStack(@NotNull final ConnectionGroup group, @NotNull final String name) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return Optional.empty();
        }

        return getStack(location.get(), name);
    }
    static Optional<ItemStack> getStack(@NotNull final Location location, @NotNull final String name) {
        final Optional<ItemDisplay> itemDisplay = QuapticBlock.getItemDisplay(location, name);
        if (itemDisplay.isEmpty()) {
            return Optional.empty();
        }

        final ItemStack stack = itemDisplay.get().getItemStack();
        return stack == null || stack.getItemMeta() == null ? Optional.empty() : Optional.of(stack);
    }

    static void insertItem(final Location location, @NotNull final String name, @NotNull final ItemStack itemStack) {
        final Optional<ItemDisplay> itemDisplay = QuapticBlock.getItemDisplay(location, name);
        if (itemDisplay.isEmpty()) {
            return;
        }

        itemDisplay.get().setItemStack(itemStack);
    }
    static Optional<ItemStack> removeItem(@NotNull final Location location, @NotNull final String name) {
        final Optional<ItemDisplay> itemDisplay = QuapticBlock.getItemDisplay(location, name);
        if (itemDisplay.isEmpty()) {
            return Optional.empty();
        }

        final ItemStack itemStack = itemDisplay.get().getItemStack();
        itemDisplay.get().setItemStack(null);
        return Optional.ofNullable(itemStack);
    }

    default void itemHolderInteract(@NotNull final Location location, @NotNull final String name, @NotNull final Player player) {
        final Optional<ItemStack> currentStack = removeItem(location, name);
        BlockStorageAPI.set(location, Keys.BS_IS_HOLDING_ITEM, false);
        if (currentStack.isPresent() && !currentStack.get().getType().isEmpty()) {
            onRemove(location, name, currentStack.get()).ifPresent(itemStack -> ItemUtils.addOrDropItemMainHand(player, itemStack));
            return;
        }

        final ItemStack itemStack = player.getInventory().getItemInMainHand().asOne();
        if (itemStack.getType().isEmpty() || !onInsert(location, name, itemStack, player)) {
            return;
        }

        insertItem(location, name, itemStack);
        player.getInventory().getItemInMainHand().subtract();
        BlockStorageAPI.set(location, Keys.BS_IS_HOLDING_ITEM, true);
    }
    default void onBreakItemHolderBlock(final Location location, @NotNull final String name) {
        getStack(location, name).ifPresent(stack -> location.getWorld().dropItem(location, stack));
    }

    boolean onInsert(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack, @NotNull final Player player);
    Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack);
}
