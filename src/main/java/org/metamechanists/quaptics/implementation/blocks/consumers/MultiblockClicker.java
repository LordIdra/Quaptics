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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.SlimefunIsDumbUtils;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelLine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MultiblockClicker extends ConnectedBlock implements PowerAnimatedBlock {
    public static final Settings MULTIBLOCK_CLICKER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .useInterval(20)
            .minPower(3)
            .build();
    public static final Settings MULTIBLOCK_CLICKER_2_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .useInterval(10)
            .minPower(5)
            .build();
    public static final Settings MULTIBLOCK_CLICKER_3_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .useInterval(4)
            .minPower(20)
            .minFrequency(2)
            .build();

    public static final SlimefunItemStack MULTIBLOCK_CLICKER_1 = new SlimefunItemStack(
            "QP_MULTIBLOCK_CLICKER_1",
            Material.DISPENSER,
            "&7Multiblock Clicker &fI",
            Lore.create(MULTIBLOCK_CLICKER_1_SETTINGS,
                    "&7● Automatically clicks the attached multiblock",
                    "&7● Place facing the block you'd usually click to use the multiblock",
                    "&7● &eRight Click &7to enable/disable"));
    public static final SlimefunItemStack MULTIBLOCK_CLICKER_2 = new SlimefunItemStack(
            "QP_MULTIBLOCK_CLICKER_2",
            Material.DISPENSER,
            "&7Multiblock Clicker &fII",
            Lore.create(MULTIBLOCK_CLICKER_2_SETTINGS,
                    "&7● Automatically clicks the attached multiblock",
                    "&7● Place facing the block you'd usually click to use the multiblock",
                    "&7● &eRight Click &7to enable/disable"));
    public static final SlimefunItemStack MULTIBLOCK_CLICKER_3 = new SlimefunItemStack(
            "QP_MULTIBLOCK_CLICKER_3",
            Material.DISPENSER,
            "&7Multiblock Clicker &fIII",
            Lore.create(MULTIBLOCK_CLICKER_3_SETTINGS,
                    "&7● Automatically clicks the attached multiblock",
                    "&7● Place facing the block you'd usually click to use the multiblock",
                    "&7● &eRight Click &7to enable/disable"));

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public MultiblockClicker(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.35F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.CYAN_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.3F))
                .add("attachment", new ModelLine()
                        .material(Material.WHITE_CONCRETE)
                        .to(player.getFacing().getDirection().toVector3f())
                        .thickness(0.15F))
                .buildAtBlockCenter(location);
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
    protected boolean isTicker() {
        return true;
    }

    @SuppressWarnings("unused")
    @Override
    public void onTick2(@NotNull final ConnectionGroup group, @NotNull final Location location) {
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
        ticksSinceLastUpdate += QuapticTicker.INTERVAL_TICKS_2;
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
    protected boolean onRightClick(final @NotNull Location location, final @NotNull Player player) {
        final Optional<Block> multiblockBlock = getMultiblockBlock(location);
        if (multiblockBlock.isEmpty()) {
            return true;
        }

        final Optional<MultiBlockMachine> machine = SlimefunIsDumbUtils.getMultiblockMachine(multiblockBlock.get());
        if (machine.isEmpty()) {
            Language.sendLanguageMessage(player, "multiblock-clicker.not-connected-to-multiblock");
            return true;
        }

        final Optional<Link> link = getLink(location, "input");
        if (link.isEmpty() || !settings.isOperational(link)) {
            Language.sendLanguageMessage(player, "multiblock-clicker.not-powered");
            setEnabled(location, false);
            return true;
        }

        BlockStorageAPI.set(location, Keys.BS_PLAYER, player.getUniqueId());
        final boolean enabled = BlockStorageAPI.getBoolean(location, Keys.BS_ENABLED);
        onPoweredAnimation(location, !enabled);
        setEnabled(location, !enabled);
        return true;
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        doBurnoutCheck(group, "input");
    }
    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
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
