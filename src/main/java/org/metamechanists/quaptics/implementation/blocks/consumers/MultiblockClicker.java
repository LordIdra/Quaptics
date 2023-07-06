package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MultiblockClicker extends ConnectedBlock {
    private static final float PLATE_DISTANCE = 0.5F;
    private final Vector3f attachmentDisplaySize = new Vector3f(0.7F, 0.1F, 0.7F);
    private final Vector3f mainDisplaySize = new Vector3f(0.3F, 0.3F, 0.3F);

    public MultiblockClicker(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        player.getFacing();
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.LIGHT_GRAY_CONCRETE_POWDER.createBlockData())
                .setTransformation(Transformations.adjustedScale(mainDisplaySize))
                .build());
        displayGroup.addDisplay("attachment", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.WHITE_CONCRETE.createBlockData())
                .setTransformation(Transformations.adjustedScaleAndOffset(attachmentDisplaySize, player.getFacing().getDirection().toVector3f().mul(PLATE_DISTANCE)))
                .build());
        BlockStorage.addBlockInfo(location, Keys.BS_OWNER, player.getUniqueId().toString());
        BlockStorage.addBlockInfo(location, Keys.BS_FACING, player.getFacing().toString());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        //return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", (player, location, player.getFacing().getDirection())));
        return new ArrayList<>();
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Player player = Bukkit.getPlayer(BlockStorage.getLocationInfo(location.get(), Keys.BS_FACING));
        if (player == null) {
            return;
        }

        final BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(location.get(), Keys.BS_FACING));
        final Block multiblockBlock = location.get().getBlock().getRelative(face);
        if (!(BlockStorage.check(multiblockBlock) instanceof final MultiBlockMachine multiblock)) {
            return;
        }

        multiblock.onInteract(player, multiblockBlock);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        //doBurnoutCheck(group, "input");
    }
}
