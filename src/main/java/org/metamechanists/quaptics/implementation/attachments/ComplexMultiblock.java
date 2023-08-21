package org.metamechanists.quaptics.implementation.attachments;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWand;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.displaymodellib.builders.InteractionBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@FunctionalInterface
public interface ComplexMultiblock {
    Color EMPTY_COLOR = Color.fromARGB(255, 255, 255, 0);
    Color WRONG_MATERIAL_COLOR = Color.fromARGB(255, 255, 0, 0);
    Color RIGHT_MATERIAL_COLOR = Color.fromARGB(255, 0, 255, 0);
    int DISPLAY_BRIGHTNESS = 15;
    float DISPLAY_SIZE = 0.75F;
    ModelCuboid GHOST_BLOCK_DISPLAY = new ModelCuboid()
            .size(DISPLAY_SIZE)
            .brightness(DISPLAY_BRIGHTNESS);

    private static boolean isStructureBlockValid(final @NotNull Block center, final @NotNull Vector offset, final ItemStack predicted) {
        final Block actual = center.getRelative(offset.getBlockX(), offset.getBlockY(), offset.getBlockZ());
        return isStructureBlockValid(actual, predicted);
    }
    private static boolean isStructureBlockValid(final @NotNull Block actual, final ItemStack predicted) {
        final SlimefunItem predictedSlimefunItem = SlimefunItem.getByItem(predicted);
        final SlimefunItem actualSlimefunItem = BlockStorageAPI.check(actual);

        if (actualSlimefunItem != null) {
            return predictedSlimefunItem != null && predictedSlimefunItem.getId().equals(actualSlimefunItem.getId());
        }

        return predicted.getType() == actual.getType();
    }
    default boolean isStructureValid(final Block center) {
        return getStructure().entrySet().stream().allMatch(entry -> isStructureBlockValid(center, entry.getKey(), entry.getValue()));
    }

    private static @NotNull @Unmodifiable List<UUID> visualiseBlock(final @NotNull Block center, final @NotNull Vector offset, final @NotNull ItemStack itemStack) {
        final Block block = center.getRelative(offset.getBlockX(), offset.getBlockY(), offset.getBlockZ());
        if (block.getType().isEmpty()) {
            GHOST_BLOCK_DISPLAY.glow(EMPTY_COLOR);
        } else {
            GHOST_BLOCK_DISPLAY.glow(isStructureBlockValid(block, itemStack) ? RIGHT_MATERIAL_COLOR : WRONG_MATERIAL_COLOR);
        }

        final BlockDisplay blockDisplay = GHOST_BLOCK_DISPLAY
                .material(itemStack.getType())
                .build(block.getLocation().toCenterLocation());
        final Interaction interaction = new InteractionBuilder()
                .width(DISPLAY_SIZE)
                .height(DISPLAY_SIZE)
                .build(block.getLocation().toCenterLocation());

        final SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);
        final String blockName = slimefunItem != null ? slimefunItem.getItemName() : ChatUtils.humanize(itemStack.getType().name());
        final PersistentDataTraverser traverser = new PersistentDataTraverser(interaction.getUniqueId());
        traverser.set("blockName", blockName);

        return List.of(blockDisplay.getUniqueId(), interaction.getUniqueId());
    }
    default void visualiseStructure(final ItemStack wand, final Block center) {
        final List<UUID> uuids = new ArrayList<>();
        getStructure().forEach((key, value) -> uuids.addAll(visualiseBlock(center, key, value)));
        final PersistentDataTraverser traverser = new PersistentDataTraverser(wand);
        traverser.set("uuids", uuids);
        traverser.save(wand);
    }

    default void multiblockInteract(final Block center, final Player player, final ItemStack itemStack) {
        if (isStructureValid(center)) {
            Language.sendLanguageMessage(player, "multiblock.valid");
            return;
        }

        MultiblockWand.removeProjection(itemStack);
        visualiseStructure(itemStack, center);
    }
    default boolean multiblockInteract(final Block center, final @NotNull Player player) {
        final ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        final ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if (SlimefunItem.getByItem(mainHandItem) instanceof MultiblockWand) {
            multiblockInteract(center, player, mainHandItem);
            return true;
        }

        if (SlimefunItem.getByItem(offHandItem) instanceof MultiblockWand) {
            multiblockInteract(center, player, offHandItem);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    default void tickAnimation(@NotNull final Location centerLocation, final double timeSeconds) {}

    Map<Vector, ItemStack> getStructure();
}
