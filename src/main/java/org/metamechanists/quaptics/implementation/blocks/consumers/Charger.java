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
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.implementation.ChargerInfoPanel;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Charger extends ConnectedBlock implements InfoPanelBlock, ItemHolderBlock {
    public static final Settings CHARGER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack CHARGER_1 = new SlimefunItemStack(
            "QP_CHARGER_1",
            Material.LIGHT_BLUE_STAINED_GLASS,
            "&bCharger &3I",
            Lore.create(CHARGER_1_SETTINGS,
                    "&7● Charges item with Quaptic Energy Units",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    private static final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.70F, 0.30F, 0.70F);
    private static final Vector3f GLASS_DISPLAY_SIZE = new Vector3f(0.50F, 0.10F, 0.50F);
    private static final Vector3f ITEM_DISPLAY_SIZE = new Vector3f(0.50F);
    private static final Vector3f TOP_OFFSET = new Vector3f(0, 0.35F, 0);
    private static final Vector3f BOTTOM_OFFSET = new Vector3f(0, -0.35F, 0);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public Charger(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.60F;
    }
    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("mainTop", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.SMOOTH_STONE_SLAB.createBlockData("[type=top]"))
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(MAIN_DISPLAY_SIZE)
                        .translate(TOP_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("mainBottom", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.SMOOTH_STONE_SLAB.createBlockData("[type=bottom]"))
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(MAIN_DISPLAY_SIZE)
                        .translate(BOTTOM_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("glassTop", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_BLUE_STAINED_GLASS)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(GLASS_DISPLAY_SIZE)
                        .translate(TOP_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("glassBottom", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_BLUE_STAINED_GLASS)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(GLASS_DISPLAY_SIZE)
                        .translate(BOTTOM_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("item", new ItemDisplayBuilder(location.toCenterLocation())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(ITEM_DISPLAY_SIZE)
                        .buildForItemDisplay())
                .build());
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
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

        final Optional<ItemStack> stack = ItemHolderBlock.getStack(group);
        setPanelHidden(group, stack.isEmpty());
        if (stack.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(group, "input");
        if (inputLink.isEmpty() || !settings.isOperational(inputLink)) {
            return;
        }

        final ItemStack newStack = QuapticChargeableItem.chargeItem(inputLink.get(), stack.get());
        ItemHolderBlock.insertItem(location, newStack);
        updatePanel(group);
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        doBurnoutCheck(group, "input");
    }

    @Override
    public boolean onInsert(@NotNull final Location location, @NotNull final ItemStack stack, @NotNull final Player player) {
        if (!(SlimefunItem.getByItem(stack) instanceof QuapticChargeableItem)) {
            Language.sendLanguageMessage(player, "charger.not-chargeable");
            return false;
        }
        return true;
    }
    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final ItemStack stack) {
        QuapticChargeableItem.updateLore(stack);
        return Optional.of(stack);
    }

    @Override
    public BlockInfoPanel createPanel(final Location location, @NotNull final ConnectionGroup group) {
        return new ChargerInfoPanel(location, group.getId());
    }
    @Override
    public BlockInfoPanel getPanel(final InfoPanelId panelId, final ConnectionGroupId groupId) {
        return new ChargerInfoPanel(panelId, groupId);
    }
}
