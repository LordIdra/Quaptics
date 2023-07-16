package org.metamechanists.quaptics.implementation.attachments;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;

import java.util.Map;
import java.util.Optional;


@FunctionalInterface
public interface ItemProcessor {
    default boolean isProcessing(@NotNull final Location location) {
        return BlockStorageAPI.getBoolean(location, Keys.BS_CRAFT_IN_PROGRESS);
    }
    default void startProcessing(@NotNull final Location location) {
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, 0);
        BlockStorageAPI.set(location, Keys.BS_CRAFT_IN_PROGRESS, true);
    }
    default void cancelProcessing(@NotNull final Location location) {
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, 0);
        BlockStorageAPI.set(location, Keys.BS_CRAFT_IN_PROGRESS, false);
    }
    default void completeProcessing(@NotNull final Location location) {
        final Optional<ItemStack> stack = ItemHolderBlock.getStack(location, "item");
        if (stack.isEmpty()) {
            return;
        }

        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, 0);
        BlockStorageAPI.set(location, Keys.BS_CRAFT_IN_PROGRESS, false);
        ItemHolderBlock.insertItem(location, "item", getRecipes().get(stack.get()));
    }
    default void tickProcessing(@NotNull final Location location, final int tickInterval) {
        double secondsSinceCraftStarted = BlockStorageAPI.getDouble(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED);
        secondsSinceCraftStarted += (double) tickInterval / QuapticTicker.TICKS_PER_SECOND;
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, secondsSinceCraftStarted);
    }

    default boolean isValidRecipe(@NotNull final ItemStack inputStack) {
        return getRecipes().keySet().stream().anyMatch(input -> SlimefunUtils.isItemSimilar(input, inputStack, false));
    }
    Map<ItemStack, ItemStack> getRecipes();
}
