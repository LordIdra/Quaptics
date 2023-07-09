package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
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
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.SlimefunIsDumbUtils;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MultiblockClicker extends ConnectedBlock implements PowerAnimatedBlock {
    public static final Settings MULTIBLOCK_CLICKER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.35F)
            .useInterval(10)
            .minPower(7)
            .build();
    public static final SlimefunItemStack MULTIBLOCK_CLICKER_1 = new SlimefunItemStack(
            "QP_MULTIBLOCK_CLICKER_1",
            Material.DISPENSER,
            "&6Multiblock Clicker &eI",
            Lore.create(MULTIBLOCK_CLICKER_1_SETTINGS,
                    "&7● &eRight Click &7to enable/disable",
                    "&7● Automatically clicks the attached multiblock",
                    "&7● Place facing the block you'd usually click to use the multiblock"));

    private static final Vector RELATIVE_PLATE_LOCATION = new Vector(0, 0, 0.45F);
    private static final Vector3f ATTACHMENT_DISPLAY_SIZE = new Vector3f(0.15F, 0.15F, 0.85F);
    private static final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.3F, 0.3F, 0.3F);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public MultiblockClicker(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        player.getFacing();
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.CYAN_CONCRETE.createBlockData())
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(MAIN_DISPLAY_SIZE)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("attachment", new BlockDisplayBuilder(formatPointLocation(player, location, RELATIVE_PLATE_LOCATION))
                .setBlockData(Material.WHITE_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(ATTACHMENT_DISPLAY_SIZE)
                        .lookAlong(player.getFacing())
                        .buildForBlockDisplay())
                .build());
        BlockStorageAPI.set(location, Keys.BS_FACING, player.getFacing());
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input",
                formatPointLocation(player, location, inputPointLocation)));
    }
    @Override
    protected void initBlockStorage(@NotNull final Location location) {
        BlockStorageAPI.set(location, Keys.BS_TICKS_SINCE_LAST_UPDATE, 0);
        BlockStorageAPI.set(location, Keys.BS_POWERED, false);
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        final Optional<UUID> uuid = BlockStorageAPI.getUuid(location, Keys.BS_PLAYER);
        if (uuid.isEmpty()) {
            return;
        }

        final Player owner = Bukkit.getPlayer(uuid.get());
        final boolean enabled = BlockStorageAPI.getBoolean(location, Keys.BS_ENABLED);
        if (!enabled) {
            return;
        }

        final Optional<Block> multiblockBlock = getMultiblockBlock(location);
        if (multiblockBlock.isEmpty()) {
            setEnabled(location, false);
            return;
        }

        final Optional<MultiBlockMachine> machine = SlimefunIsDumbUtils.getMultiblockMachine(multiblockBlock.get());
        if (machine.isEmpty()) {
            setEnabled(location, false);
            return;
        }

        final Optional<Link> link = getLink(location, "input");
        if (link.isEmpty() || !settings.isOperational(link)) {
            setEnabled(location, false);
            return;
        }

        int ticksSinceLastUpdate = BlockStorageAPI.getInt(location, Keys.BS_TICKS_SINCE_LAST_UPDATE);
        ticksSinceLastUpdate += QuapticTicker.INTERVAL_TICKS;
        if (owner == null || ticksSinceLastUpdate < settings.getUseInterval()) {
            BlockStorageAPI.set(location, Keys.BS_TICKS_SINCE_LAST_UPDATE, ticksSinceLastUpdate);
            return;
        }

        final int usesInThisTick = (int) (ticksSinceLastUpdate / settings.getUseInterval());
        machine.get().onInteract(owner, multiblockBlock.get());
        ticksSinceLastUpdate -= usesInThisTick * settings.getUseInterval();
        ticksSinceLastUpdate %= settings.getUseInterval();
        BlockStorageAPI.set(location, Keys.BS_TICKS_SINCE_LAST_UPDATE, ticksSinceLastUpdate);
    }
    @Override
    protected void onRightClick(final @NotNull Location location, final @NotNull Player player) {
        final Optional<Block> multiblockBlock = getMultiblockBlock(location);
        if (multiblockBlock.isEmpty()) {
            return;
        }

        final Optional<MultiBlockMachine> machine = SlimefunIsDumbUtils.getMultiblockMachine(multiblockBlock.get());
        if (machine.isEmpty()) {
            Language.sendLanguageMessage(player, "multiblock-clicker.not-connected-to-multiblock");
            return;
        }

        final Optional<Link> link = getLink(location, "input");
        if (link.isEmpty() || !settings.isOperational(link)) {
            Language.sendLanguageMessage(player, "multiblock-clicker.not-powered");
            setEnabled(location, false);
            return;
        }

        BlockStorageAPI.set(location, Keys.BS_PLAYER, player.getUniqueId());
        final boolean enabled = BlockStorageAPI.getBoolean(location, Keys.BS_ENABLED);
        onPoweredAnimation(location, !enabled);
        setEnabled(location, !enabled);
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        doBurnoutCheck(group, "input");
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        getDisplay(location, "main").ifPresent(value -> value.setBrightness(new Brightness(powered ? Utils.BRIGHTNESS_ON : Utils.BRIGHTNESS_OFF, 0)));
    }

    private void setEnabled(final Location location, final boolean enabled) {
        BlockStorageAPI.set(location, Keys.BS_ENABLED, enabled);
        onPoweredAnimation(location, enabled);
    }
    private static Optional<Block> getMultiblockBlock(final @NotNull Location location) {
        final Optional<BlockFace> face = BlockStorageAPI.getBlockFace(location, Keys.BS_FACING);
        return face.map(blockFace -> location.getBlock().getRelative(blockFace));
    }
}
