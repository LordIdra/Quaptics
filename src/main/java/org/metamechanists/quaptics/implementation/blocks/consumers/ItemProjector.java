package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.ConfigPanelBlock;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.config.ConfigPanel;
import org.metamechanists.quaptics.panels.config.implementation.ItemProjectorConfigPanel;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelItem;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;
import org.metamechanists.quaptics.utils.transformations.TransformationUtils;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class ItemProjector extends ConnectedBlock implements ItemHolderBlock, PowerAnimatedBlock, ConfigPanelBlock {
    public static final Settings ITEM_PROJECTOR_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack ITEM_PROJECTOR = new SlimefunItemStack(
            "QP_ITEM_PROJECTOR",
            Material.LIGHT_BLUE_STAINED_GLASS,
            "&6Item Projector",
            Lore.create(ITEM_PROJECTOR_SETTINGS,
                    "&7● Displays a hologram of an inserted item",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    private static final Vector RELATIVE_PANEL_LOCATION = new Vector(0, 0, -0.51);
    public static final double MAX_SIZE = 20;
    public static final double MAX_HEIGHT = 20;
    public static final int MAX_MODE = 3;
    private static final double HEIGHT_MULTIPLY = 0.1;
    private static final double SIZE_MULTIPLY = 0.1;
    private static final Vector3f ITEM_DISPLAY_ADDITIONAL_SIZE = new Vector3f(0.1F);
    private static final Vector3f ITEM_DISPLAY_ADDITIONAL_OFFSET = new Vector3f(0, 0.6F, 0);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public ItemProjector(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.6F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.LIGHT_GRAY_CONCRETE)
                        .location(0, -0.2F, 0)
                        .size(1.0F, 0.6F, 1.0F))
                .add("prism", new ModelCuboid()
                        .block(Material.LIGHT_BLUE_STAINED_GLASS.createBlockData())
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .location(0, 0.1F, 0)
                        .size(0.6F, 0.1F, 0.6F)
                        .rotation(Math.PI / 4))
                .add("item", new ModelItem()
                        .viewRange(Utils.VIEW_RANGE_OFF)
                        .billboard(Billboard.VERTICAL)
                        .brightness(Utils.BRIGHTNESS_ON))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }
    @Override
    protected void initBlockStorage(@NotNull final Location location) {
        BlockStorageAPI.set(location, Keys.BS_HEIGHT, 0);
        BlockStorageAPI.set(location, Keys.BS_SIZE, ITEM_DISPLAY_ADDITIONAL_SIZE.x);
        BlockStorageAPI.set(location, Keys.BS_MODE, 0);
        onConfigUpdated(location);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        onPlaceConfigPanelBlock(event);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakConfigPanelBlock(location);
        onBreakItemHolderBlock(location, "item");
    }
    @Override
    protected boolean onRightClick(final @NotNull Location location, final @NotNull Player player) {
        itemHolderInteract(location, "item", player);
        return true;
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<Link> inputLink = getLink(group, "input");
        onPoweredAnimation(location, inputLink.isPresent() && settings.isOperational(inputLink));
    }
    @Override
    public boolean onInsert(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack, @NotNull final Player player) {
        return true;
    }
    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack) {
        return Optional.of(stack);
    }
    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location, "prism", powered);
        visibilityAnimation(location, "item", powered);
    }

    @Override
    public ConfigPanel createPanel(final Location location, final Player player, @NotNull final ConnectionGroup group) {
        return new ItemProjectorConfigPanel(formatPointLocation(player, location, RELATIVE_PANEL_LOCATION), group.getId(),
                (float) TransformationUtils.yawToCardinalDirection(player.getEyeLocation().getYaw()));
    }
    @Override
    public ConfigPanel getPanel(final ConfigPanelId panelId, final ConnectionGroupId groupId) {
        return new ItemProjectorConfigPanel(panelId, groupId);
    }

    private static @NotNull Matrix4f calculateItemTransformation(final double size, final double offset) {
        return new TransformationMatrixBuilder()
                .translate(new Vector3f(ITEM_DISPLAY_ADDITIONAL_OFFSET).add(new Vector3f(0, (float) offset, 0)))
                .scale(new Vector3f(ITEM_DISPLAY_ADDITIONAL_SIZE).add(new Vector3f((float) size)))
                .buildForItemDisplay();
    }
    public static void onConfigUpdated(final Location location) {
        final Optional<Display> display = getDisplay(location, "item");
        if (display.isEmpty() || !(display.get() instanceof final ItemDisplay itemDisplay)) {
            return;
        }

        itemDisplay.setBillboard(Billboard.values()[BlockStorageAPI.getInt(location, Keys.BS_MODE)]);
        itemDisplay.setTransformationMatrix(calculateItemTransformation(
                BlockStorageAPI.getDouble(location, Keys.BS_SIZE) * SIZE_MULTIPLY,
                BlockStorageAPI.getDouble(location, Keys.BS_HEIGHT) * HEIGHT_MULTIPLY));
    }
}
