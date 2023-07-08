package org.metamechanists.quaptics.utils;

import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class SlimefunIsDumbUtils {
    // https://github.com/Slimefun/Slimefun4/blob/master/src/main/java/io/github/thebusybiscuit/slimefun4/implementation/listeners/MultiBlockListener.java#L65
    public Optional<MultiBlockMachine> getMultiblockMachine(final Block block) {
        for (final MultiBlock multiBlock : Slimefun.getRegistry().getMultiBlocks()) {
            if (!(multiBlock.getSlimefunItem() instanceof MultiBlockMachine multiBlockMachine)) {
                continue;
            }

            final Block center = block.getRelative(multiBlock.getTriggerBlock());
            if (compareMaterials(center, multiBlock.getStructure(), multiBlock.isSymmetric())) {
                return Optional.of(multiBlockMachine);
            }
        }
        
        return Optional.empty();
    }

    @ParametersAreNonnullByDefault
    private boolean compareMaterials(final Block block, final Material[] blocks, final boolean onlyTwoWay) {
        if (!compareMaterialsVertical(block, blocks[1], blocks[4], blocks[7])) {
            return false;
        }

        final BlockFace[] directions = onlyTwoWay
                ? new BlockFace[] { BlockFace.NORTH, BlockFace.EAST }
                : new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

        return Arrays.stream(directions).anyMatch(direction ->
                compareMaterialsVertical(block.getRelative(direction), blocks[0], blocks[3], blocks[6])
                        && compareMaterialsVertical(block.getRelative(direction.getOppositeFace()), blocks[2], blocks[5], blocks[8]));
    }

    private boolean compareMaterialsVertical(final Block block, @Nullable final Material top, @Nullable final Material center, @Nullable final Material bottom) {
        return (center == null || equals(block.getType(), center))
                && (top == null || equals(block.getRelative(BlockFace.UP).getType(), top))
                && (bottom == null || equals(block.getRelative(BlockFace.DOWN).getType(), bottom));
    }

    @ParametersAreNonnullByDefault
    private boolean equals(final Material a, final Material b) {
        return a == b || MultiBlock.getSupportedTags().stream().anyMatch(tag -> tag.isTagged(a) && tag.isTagged(b));
    }
}
