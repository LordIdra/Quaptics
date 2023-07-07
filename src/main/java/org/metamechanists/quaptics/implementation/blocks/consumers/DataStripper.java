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
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.ProgressBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.panels.info.implementation.ProgressInfoPanel;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class DataStripper extends ConnectedBlock implements PanelBlock, ItemHolderBlock, ProgressBlock {
    private static final double MAX_PROGRESS_DIFFERENCE = 0.00001;
    private final Vector3f mainDisplaySize = new Vector3f(0.5F, 0.3F, 0.5F);
    private final Vector3f glassDisplaySize = new Vector3f(0.4F, 0.15F, 0.4F);
    private final Vector3f itemDisplaySize = new Vector3f(0.5F);
    private final Vector3f topOffset = new Vector3f(0, 0.35F, 0);
    private final Vector3f bottomOffset = new Vector3f(0, -0.35F, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public DataStripper(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("mainTop", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.SMOOTH_STONE_SLAB.createBlockData("[type=top]"))
                .setTransformation(Transformations.adjustedScaleOffset(mainDisplaySize, topOffset))
                .build());
        displayGroup.addDisplay("mainBottom", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.SMOOTH_STONE_SLAB.createBlockData("[type=bottom]"))
                .setTransformation(Transformations.adjustedScaleOffset(mainDisplaySize, bottomOffset))
                .build());
        displayGroup.addDisplay("glassTop", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.ORANGE_STAINED_GLASS)
                .setTransformation(Transformations.adjustedScaleOffset(glassDisplaySize, topOffset))
                .build());
        displayGroup.addDisplay("glassBottom", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.ORANGE_STAINED_GLASS)
                .setTransformation(Transformations.adjustedScaleOffset(glassDisplaySize, bottomOffset))
                .build());
        displayGroup.addDisplay("item", new ItemDisplayBuilder(location.toCenterLocation())
                .setTransformation(Transformations.unadjustedScale(itemDisplaySize))
                .build());
        ProgressBlock.setProgress(location, 0);
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    @Override
    public BlockInfoPanel createPanel(final InfoPanelId panelId, final ConnectionGroupId groupId) {
        return new ProgressInfoPanel(panelId, groupId);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        final Location location = event.getBlock().getLocation();
        final Optional<ConnectionGroup> optionalGroup = getGroup(location);
        optionalGroup.ifPresent(group -> PanelBlock.setPanelId(location, new ProgressInfoPanel(location, group.getId()).getId()));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        final Optional<InfoPanelId> panelId = PanelBlock.getPanelId(location);
        final Optional<InfoPanelContainer> panel = panelId.isPresent() ? panelId.get().get() : Optional.empty();
        panel.ifPresent(InfoPanelContainer::remove);
        ItemHolderBlock.getStack(location).ifPresent(stack -> location.getWorld().dropItem(location, stack));
    }

    @Override
    protected void onRightClick(final @NotNull Location location, final @NotNull Player player) {
        interact(location, player);
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        setPanelHidden(group, ItemHolderBlock.getStack(group).isEmpty());

        final Optional<Link> inputLink = getLink(group, "input");
        if (inputLink.isEmpty() || !settings.isOperational(inputLink)) {
            return;
        }

        if (ItemHolderBlock.getStack(location.get()).isPresent()) {
            ProgressBlock.updateProgress(location.get(), settings.getTimePerItem());
        }

        updatePanel(group);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        doBurnoutCheck(group, "input");
    }

    @Override
    public boolean onInsert(final @NotNull ItemStack stack, @NotNull final Player player) {
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
