package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.attachments.ProgressBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.implementation.ProgressInfoPanel;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelItem;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class DataStripper extends ConnectedBlock implements InfoPanelBlock, ItemHolderBlock, ProgressBlock {
    public static final Settings DATA_STRIPPER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .timePerItem(10)
            .minPower(6)
            .build();
    public static final SlimefunItemStack DATA_STRIPPER_1 = new SlimefunItemStack(
            "QP_DATA_STRIPPER_1",
            Material.ORANGE_STAINED_GLASS,
            "&6Data Stripper &eI",
            Lore.create(DATA_STRIPPER_1_SETTINGS,
                    "&7● Converts Slimefun heads into placeable vanilla heads",
                    "&7● &eRight Click &7with an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    private static final double MAX_PROGRESS_DIFFERENCE = 0.00001;

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public DataStripper(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.40F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("mainTop", new ModelCuboid()
                        .block(Material.SMOOTH_STONE_SLAB.createBlockData("[type=top]"))
                        .location(0, 0.35F, 0)
                        .size(0.5F, 0.3F, 0.5F))
                .add("mainBottom", new ModelCuboid()
                        .block(Material.SMOOTH_STONE_SLAB.createBlockData("[type=bottom]"))
                        .location(0, -0.35F, 0)
                        .size(0.5F, 0.3F, 0.5F))
                .add("glassTop", new ModelCuboid()
                        .material(Material.ORANGE_STAINED_GLASS)
                        .location(0, 0.35F, 0)
                        .size(0.3F, 0.15F, 0.3F))
                .add("glassBottom", new ModelCuboid()
                        .material(Material.ORANGE_STAINED_GLASS)
                        .location(0, -0.35F, 0)
                        .size(0.3F, 0.15F, 0.3F))
                .add("item", new ModelItem()
                        .location(0, 0.25F, 0)
                        .size(0.5F))
                .build(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }
    @Override
    protected void initBlockStorage(@NotNull final Location location) {
        ProgressBlock.setProgress(location, 0);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        onPlaceInfoPanelBlock(event);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakInfoPanelBlock(location);
        onBreakItemHolderBlock(location);
    }
    @Override
    protected void onRightClick(final @NotNull Location location, final @NotNull Player player) {
        itemHolderInteract(location, player);
    }
    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (!BlockStorageAPI.getBoolean(location, Keys.BS_IS_HOLDING_ITEM)) {
            setPanelHidden(group, true);
            return;
        }

        setPanelHidden(group, ItemHolderBlock.getStack(group).isEmpty());

        final Optional<Link> inputLink = getLink(group, "input");
        if (inputLink.isEmpty() || !settings.isOperational(inputLink)) {
            return;
        }

        if (ItemHolderBlock.getStack(location).isPresent()) {
            ProgressBlock.updateProgress(location, settings.getTimePerItem());
        }

        updatePanel(group);
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        doBurnoutCheck(group, "input");
    }
    @Override
    public boolean onInsert(@NotNull final Location location, final @NotNull ItemStack stack, @NotNull final Player player) {
        if (SlimefunItem.getByItem(stack) == null || stack.getType() != Material.PLAYER_HEAD) {
            Language.sendLanguageMessage(player, "data-stripper.not-slimefun-head");
            return false;
        }

        return true;
    }
    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, final @NotNull ItemStack stack) {
        final double progress = ProgressBlock.getProgress(location);
        ProgressBlock.setProgress(location, 0);
        return Math.abs(progress - settings.getTimePerItem()) < MAX_PROGRESS_DIFFERENCE
                ? Optional.of(stripData(stack))
                : Optional.of(stack);

    }

    @Override
    public BlockInfoPanel createPanel(final Location location, @NotNull final ConnectionGroup group) {
        return new ProgressInfoPanel(location, group.getId());
    }
    @Override
    public BlockInfoPanel getPanel(final InfoPanelId panelId, final ConnectionGroupId groupId) {
        return new ProgressInfoPanel(panelId, groupId);
    }

    private static @NotNull ItemStack stripData(final @NotNull ItemStack inputStack) {
        final ItemStack intermediaryStack = inputStack.clone();
        final SkullMeta itemMeta = (SkullMeta) intermediaryStack.getItemMeta();
        final ItemStack outputStack = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta outputMeta = (SkullMeta) outputStack.getItemMeta();

        outputMeta.setOwnerProfile(itemMeta.getOwnerProfile());
        outputStack.setItemMeta(outputMeta);

        return outputStack;
    }
}
