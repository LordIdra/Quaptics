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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.implementation.ChargerInfoPanel;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelItem;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Charger extends ConnectedBlock implements InfoPanelBlock, ItemHolderBlock, PowerAnimatedBlock {
    public static final Settings CHARGER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final Settings CHARGER_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .minFrequency(2)
            .build();
    public static final Settings CHARGER_3_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .minFrequency(100)
            .build();
    public static final Settings CHARGER_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .minFrequency(1200)
            .build();

    public static final SlimefunItemStack CHARGER_1 = new SlimefunItemStack(
            "QP_CHARGER_1",
            Material.POLISHED_DEEPSLATE_SLAB,
            "&7Charger &fI",
            Lore.create(CHARGER_1_SETTINGS,
                    "&7● Charges Quaptic items",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));
    public static final SlimefunItemStack CHARGER_2 = new SlimefunItemStack(
            "QP_CHARGER_2",
            Material.POLISHED_DEEPSLATE_SLAB,
            "&7Charger &8II",
            Lore.create(CHARGER_2_SETTINGS,
                    "&7● Charges Quaptic items",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));
    public static final SlimefunItemStack CHARGER_3 = new SlimefunItemStack(
            "QP_CHARGER_3",
            Material.POLISHED_DEEPSLATE_SLAB,
            "&7Charger &8III",
            Lore.create(CHARGER_3_SETTINGS,
                    "&7● Charges Quaptic items",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));
    public static final SlimefunItemStack CHARGER_4 = new SlimefunItemStack(
            "QP_CHARGER_4",
            Material.POLISHED_DEEPSLATE_SLAB,
            "&7Charger &8IV",
            Lore.create(CHARGER_4_SETTINGS,
                    "&7● Charges Quaptic items",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public Charger(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.60F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("mainTop", new ModelCuboid()
                        .block(Material.POLISHED_DEEPSLATE_SLAB.createBlockData("[type=top]"))
                        .location(0, 0.35F, 0)
                        .size(0.6F, 0.3F, 0.6F))
                .add("mainBottom", new ModelCuboid()
                        .block(Material.POLISHED_DEEPSLATE_SLAB.createBlockData("[type=bottom]"))
                        .location(0, -0.35F, 0)
                        .size(0.6F, 0.3F, 0.6F))
                .add("glassTop", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .location(0, 0.35F, 0)
                        .size(0.4F, 0.15F, 0.4F))
                .add("glassBottom", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .location(0, -0.35F, 0)
                        .size(0.4F, 0.15F, 0.4F))
                .add("item", new ModelItem()
                        .facing(player.getFacing())
                        .size(0.5F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    @Override
    protected boolean isTicker() {
        return true;
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
        onBreakItemHolderBlock(location, "item");
    }
    @Override
    protected boolean onRightClick(final @NotNull Location location, final @NotNull Player player) {
        itemHolderInteract(location, "item", player);
        return true;
    }
    @SuppressWarnings("unused")
    @Override
    public void onTick10(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        final boolean hasItem = BlockStorageAPI.getBoolean(location, Keys.BS_IS_HOLDING_ITEM);
        setPanelHidden(group, !hasItem);
        if (!hasItem) {
            return;
        }

        final Optional<ItemStack> stack = ItemHolderBlock.getStack(group, "item");
        if (stack.isEmpty()) {
            return;
        }

        if (!(SlimefunItem.getByItem(stack.get()) instanceof final QuapticChargeableItem chargeableItem)) {
            return;
        }

        final Optional<Link> inputLink = getLink(group, "input");
        if (inputLink.isEmpty() || !settings.isOperational(inputLink)) {
            return;
        }

        final ItemStack newStack = chargeableItem.chargeItem(inputLink.get(), stack.get(), QuapticTicker.INTERVAL_TICKS_10);
        ItemHolderBlock.insertItem(location, "item", newStack);
        updatePanel(group);
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        doBurnoutCheck(group, "input");
        final Optional<Link> inputLink = getLink(location, "input");
        onPoweredAnimation(location, settings.isOperational(inputLink));
    }
    @Override
    public boolean onInsert(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack, @NotNull final Player player) {
        if (!(SlimefunItem.getByItem(stack) instanceof QuapticChargeableItem)) {
            Language.sendLanguageMessage(player, "charger.not-chargeable");
            return false;
        }
        return true;
    }
    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack) {
        QuapticChargeableItem.updateLore(stack);
        return Optional.of(stack);
    }
    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location, "glassTop", powered);
        brightnessAnimation(location, "glassBottom", powered);
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
