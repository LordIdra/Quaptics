package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PoweredBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.SlimefunIsDumbUtils;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MultiblockClicker extends ConnectedBlock implements PoweredBlock, PowerAnimatedBlock {
    private static final Vector RELATIVE_PLATE_LOCATION = new Vector(0, 0, 0.5F);
    private static final Brightness BRIGHTNESS_ON = new Brightness(15, 0);
    private static final Brightness BRIGHTNESS_OFF = new Brightness(3, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector3f attachmentDisplaySize = new Vector3f(0.15F, 0.15F, 0.8F);
    private final Vector3f mainDisplaySize = new Vector3f(0.3F, 0.3F, 0.3F);

    public MultiblockClicker(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        player.getFacing();
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.CYAN_CONCRETE.createBlockData())
                .setTransformation(Transformations.adjustedScale(mainDisplaySize))
                .build());
        displayGroup.addDisplay("attachment", new BlockDisplayBuilder(formatPointLocation(player, location, RELATIVE_PLATE_LOCATION))
                .setBlockData(Material.WHITE_CONCRETE.createBlockData())
                .setTransformation(Transformations.lookAlong(attachmentDisplaySize, player.getFacing().getDirection().toVector3f()))
                .build());
        BlockStorage.addBlockInfo(location, Keys.BS_OWNER, player.getUniqueId().toString());
        BlockStorage.addBlockInfo(location, Keys.BS_FACING, player.getFacing().toString());
        setPowered(location, false);
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input",
                formatPointLocation(player, location, inputPointLocation)));
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(location.get(), Keys.BS_FACING));
        final Block multiblockBlock = location.get().getBlock().getRelative(face);
        final Player owner = Bukkit.getPlayer(UUID.fromString(BlockStorage.getLocationInfo(location.get(), Keys.BS_OWNER)));
        final boolean enabled = SlimefunIsDumbUtils.getMultiblockMachine(multiblockBlock).isPresent()
                && isPowered(location.get())
                && owner != null;

        onPoweredAnimation(location.get(), enabled);

        if (!enabled) {
            return;
        }

        SlimefunIsDumbUtils.getMultiblockMachine(multiblockBlock).ifPresent(block -> block.onInteract(owner, multiblockBlock));
    }

    @Override
    protected void onRightClick(final Location location, final Player player) {
        setPowered(location, !isPowered(location));
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        //doBurnoutCheck(group, "input");
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        getDisplay(location, "main").ifPresent(value -> value.setBrightness(powered ? BRIGHTNESS_ON : BRIGHTNESS_OFF));
    }
}
