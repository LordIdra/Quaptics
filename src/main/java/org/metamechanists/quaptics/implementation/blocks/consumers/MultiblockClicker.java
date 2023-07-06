package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
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
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.SlimefunIsDumbUtils;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MultiblockClicker extends ConnectedBlock implements PowerAnimatedBlock {
    private static final Vector RELATIVE_PLATE_LOCATION = new Vector(0, 0, 0.5F);
    private static final Brightness BRIGHTNESS_ON = new Brightness(15, 0);
    private static final Brightness BRIGHTNESS_OFF = new Brightness(3, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector3f attachmentDisplaySize = new Vector3f(0.15F, 0.15F, 0.8F);
    private final Vector3f mainDisplaySize = new Vector3f(0.3F, 0.3F, 0.3F);

    public MultiblockClicker(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                             final Settings settings) {
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
        BlockStorageAPI.set(location, Keys.BS_TICKS_SINCE_LAST_UPDATE, 0);
        BlockStorageAPI.set(location, Keys.BS_OWNER, player.getUniqueId());
        BlockStorageAPI.set(location, Keys.BS_FACING, player.getFacing());
        BlockStorageAPI.set(location, Keys.BS_POWERED, false);
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

        final Optional<Link> link = getLink(location.get(), "input");
        if (link.isEmpty() || settings.isOperational(link)) {
            onPoweredAnimation(location.get(), false);
            return;
        }

        final Optional<BlockFace> face = BlockStorageAPI.getBlockFace(location.get(), Keys.BS_FACING);
        if (face.isEmpty()) {
            return;
        }

        final Optional<UUID> uuid = BlockStorageAPI.getUuid(location.get(), Keys.BS_OWNER);
        if (uuid.isEmpty()) {
            return;
        }

        final Player owner = Bukkit.getPlayer(uuid.get());
        final Block multiblockBlock = location.get().getBlock().getRelative(face.get());
        final boolean enabled = SlimefunIsDumbUtils.getMultiblockMachine(multiblockBlock).isPresent()
                && BlockStorageAPI.getBoolean(location.get(), Keys.BS_ENABLED)
                && owner != null;

        onPoweredAnimation(location.get(), enabled);

        double ticksSinceLastUpdate = BlockStorageAPI.getInt(location.get(), Keys.BS_TICKS_SINCE_LAST_UPDATE);
        if (!enabled || ticksSinceLastUpdate < settings.getUseInterval()) {
            return;
        }

        final int usesInThisTick = (int) (ticksSinceLastUpdate / settings.getUseInterval());

        SlimefunIsDumbUtils.getMultiblockMachine(multiblockBlock).ifPresent(block -> block.onInteract(owner, multiblockBlock));
        ticksSinceLastUpdate -= usesInThisTick * settings.getUseInterval();
        ticksSinceLastUpdate %= settings.getUseInterval();
        BlockStorageAPI.set(location.get(), Keys.BS_TICKS_SINCE_LAST_UPDATE, ticksSinceLastUpdate);
    }

    @Override
    protected void onRightClick(final Location location, final Player player) {
        final boolean enabled = BlockStorageAPI.getBoolean(location, Keys.BS_ENABLED);
        BlockStorageAPI.set(location, Keys.BS_POWERED, !enabled);
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
